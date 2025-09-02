package com.xpxp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UnknownProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(UnknownProjectApplication.class, args);
    }

}
