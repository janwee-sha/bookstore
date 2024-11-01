package com.janwee.bookstore.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
@EnableConfigServer
@RefreshScope
public class ConfigApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ConfigApplication.class);
        app.setAdditionalProfiles("native");
        app.run(args);
    }

}
