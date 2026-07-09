package com.janwee.bookstore.order.consumer;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.janwee.bookstore.order.southbound.adapter.service.BookFeignClient;
import feign.Feign;
import feign.FeignException;
import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Retryer;
import feign.codec.Decoder;
import feign.slf4j.Slf4jLogger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

class BookExistenceHttpContractTest {

    private WireMockServer wireMockServer;
    private BookFeignClient bookClient;

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(0);
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());

        bookClient = Feign.builder()
                .contract(new SpringMvcContract())
                .decoder(new ResponseEntityDecoder())
                .logger(new Slf4jLogger(BookFeignClient.class))
                .logLevel(Logger.Level.FULL)
                .options(new Request.Options(5, TimeUnit.SECONDS, 5, TimeUnit.SECONDS, true))
                .retryer(Retryer.NEVER_RETRY)
                .target(TestableBookFeignClient.class, "http://localhost:" + wireMockServer.port());
    }

    @AfterEach
    void teardown() {
        wireMockServer.stop();
    }

    @Test
    void feignClientInterfaceDefinesCorrectContract() throws Exception {
        Method checkBook = BookFeignClient.class.getMethod("checkBook", Long.class);

        FeignClient feignClient = BookFeignClient.class.getAnnotation(FeignClient.class);
        assertNotNull(feignClient);
        assertEquals("book", feignClient.value());

        RequestMapping requestMapping = checkBook.getAnnotation(RequestMapping.class);
        assertNotNull(requestMapping);
        assertArrayEquals(new RequestMethod[]{RequestMethod.HEAD}, requestMapping.method());
        assertArrayEquals(new String[]{"books/{id}"}, requestMapping.value());

        PathVariable pathVariable = checkBook.getParameters()[0].getAnnotation(PathVariable.class);
        assertNotNull(pathVariable);
        assertEquals("id", pathVariable.value());
    }

    @Test
    void clientSendsHeadRequestToCorrectPath() {
        stubFor(head(urlEqualTo("/books/123"))
                .willReturn(aResponse().withStatus(200)));

        bookClient.checkBook(123L);

        verify(headRequestedFor(urlEqualTo("/books/123")));
    }

    @Test
    void clientReceives200WhenBookExists() {
        stubFor(head(urlEqualTo("/books/1"))
                .willReturn(aResponse().withStatus(200)));

        ResponseEntity<Void> response = bookClient.checkBook(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void clientReceives404WhenBookNotFound() {
        stubFor(head(urlEqualTo("/books/9999"))
                .willReturn(aResponse().withStatus(404)));

        FeignException.NotFound ex = assertThrows(FeignException.NotFound.class,
                () -> bookClient.checkBook(9999L));
        assertEquals(404, ex.status());
    }

    interface TestableBookFeignClient extends BookFeignClient {
    }

    static class ResponseEntityDecoder implements Decoder {
        @Override
        public Object decode(Response response, Type type) {
            return ResponseEntity.status(response.status()).build();
        }
    }
}
