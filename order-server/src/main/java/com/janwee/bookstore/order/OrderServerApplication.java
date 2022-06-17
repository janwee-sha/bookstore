package com.janwee.bookstore.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.support.MessageBuilder;

@EnableDiscoveryClient
@SpringBootApplication
@EnableCircuitBreaker
@EnableBinding(Processor.class)
public class OrderServerApplication {
    @Autowired
    private Processor processor;

    public static void main(String[] args) {
        SpringApplication.run(OrderServerApplication.class, args);
    }

    public void sendHello() {
        processor.output().send(MessageBuilder.withPayload("Hello message from Order Service").build());
    }

}
