package com.janwee.bookstore.common.infrastructure.test;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class AbstractSpringDocTest {
    public static String className;
    protected MockMvc mockMvc;

    public static String getContent(String fileName){
        try {
            Path path= Paths.get(AbstractSpringDocTest.class.getClassLoader().getResource(fileName).toURI());
            byte[] fileBytes= Files.readAllBytes(path);
            return new String(fileBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read file: "+fileName,e);
        }
    }

    /*public void testApp() throws Exception{
        className=getClass().getSimpleName();
        String testNumber=className.replaceAll("[^0-9]","");
        MvcResult mockMvcResult=mockMvc.perform(get())
    }*/
}
