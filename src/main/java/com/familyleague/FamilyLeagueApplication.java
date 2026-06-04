package com.familyleague;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FamilyLeagueApplication {

    public static void main(String[] args) {
        SpringApplication.run(FamilyLeagueApplication.class, args);
    }
}
