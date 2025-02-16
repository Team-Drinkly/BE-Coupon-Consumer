package com.drinkhere.drinklykafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class DrinklyKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DrinklyKafkaApplication.class, args);
    }

}
