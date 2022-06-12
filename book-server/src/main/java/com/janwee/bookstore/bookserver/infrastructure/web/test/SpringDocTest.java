package com.janwee.bookstore.bookserver.infrastructure.web.test;

import com.janwee.bookstore.common.infrastructure.test.AbstractSpringDocTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SpringDocTest extends AbstractSpringDocTest {
//    @Test
    public void shouldDisplaySwaggerUiPage() throws Exception {
        mockMvc.perform(get("swagger-ui/index.html"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Swagger UI")));
    }

//    @Test
    public void originIndex() throws Exception{
        super.checkJS();
    }

//    @SpringBootApplication
    static class SpringDocTestApp{
    }
}
