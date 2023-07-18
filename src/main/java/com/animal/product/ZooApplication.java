package com.animal.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.animal", "com.zoo.party.client"})
public class ZooApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZooApplication.class, args);
    }

}
