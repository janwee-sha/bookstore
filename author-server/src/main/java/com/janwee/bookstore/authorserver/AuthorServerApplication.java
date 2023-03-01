package com.janwee.bookstore.authorserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AuthorServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorServerApplication.class, args);
    }

}
