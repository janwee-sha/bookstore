package com.janwee.bookstore.bookserver;

import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.function.Function;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@EnableCircuitBreaker
public class BookServerApplication {

    @Bean
    @RouterOperations({
            @RouterOperation(method = RequestMethod.GET, operation = @Operation(description = "Uppercase",
                    operationId = "uppercase", tags = "uppercase"))
    })
    public Function<String, String> uppercase() {
        return String::toUpperCase;
    }

    public static void main(String[] args) {
        SpringApplication.run(BookServerApplication.class, args);
    }

}
