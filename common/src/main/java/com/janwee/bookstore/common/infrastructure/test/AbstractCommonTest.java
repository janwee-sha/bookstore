package com.janwee.bookstore.common.infrastructure.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

//@AutoConfigureMockMvc
public abstract class AbstractCommonTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractCommonTest.class);

//    @Autowired
    protected MockMvc mockMvc;

    public static String getContent(String fileName) {
        try {
            Path path = Paths.get(AbstractSpringDocTest.class.getClassLoader().getResource(fileName).toURI());
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            StringBuilder sb = new StringBuilder();
            for (String line : lines) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to read file: " + fileName, e);
        }
    }
}
