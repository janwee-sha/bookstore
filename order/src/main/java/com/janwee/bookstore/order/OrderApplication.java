package com.janwee.bookstore.order;

import com.janwee.bookstore.order.northbound.message.InputChannels;
import com.janwee.bookstore.order.southbound.message.OutputChannels;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.janwee.bookstore.foundation", "com.janwee.bookstore.order"})
@EnableFeignClients
@RefreshScope
@EnableBinding({InputChannels.class, OutputChannels.class})
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

}
