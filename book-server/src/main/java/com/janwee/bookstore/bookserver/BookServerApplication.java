package com.janwee.bookstore.bookserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.function.Consumer;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@Slf4j
public class BookServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookServerApplication.class, args);
    }

    @Bean
    public Consumer<String> textConsumer() {
        return s -> log.info("Received {}", s);
    }
}
