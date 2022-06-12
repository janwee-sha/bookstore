package com.janwee.bookstore.common.infrastructure.test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
public abstract class AbstractSpringDocTest extends AbstractCommonTest {
    public static String className;

    protected void checkJS(String fileName, String uri) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(uri)).andExpect(status().isOk()).andReturn();
        String transformedIndex = mvcResult.getResponse().getContentAsString();
        assertTrue(transformedIndex.contains("window.ui"));
        assertEquals(getContent(fileName), transformedIndex.replace("\r", ""));
    }

    protected void checkJS(String fileName) throws Exception {

    }

    protected void checkJS() throws Exception{

    }
}
