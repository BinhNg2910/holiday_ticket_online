package com.ticketplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ticketplatform.ddd"})
public class StartApplication {
    public static void main(String[] args) {
        System.out.println("Hello and welcome!");
        SpringApplication.run(StartApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        System.out.println("Hello and welcome!");
        return new RestTemplate();
    }
}