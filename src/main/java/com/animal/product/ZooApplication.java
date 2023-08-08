package com.animal.product;

import com.zoo.friend.OpenAIClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.animal"})
public class ZooApplication {


    @Value("${zoo.gptKey}")
    private String apikey;

    public static void main(String[] args) {
        SpringApplication.run(ZooApplication.class, args);
    }

    @Bean
    public OpenAIClient openAIClient(){
        OpenAIClient client = OpenAIClient.Party()
                .apikey(apikey)
                .partyRun();

        return client;
    }
}
