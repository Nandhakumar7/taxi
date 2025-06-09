package com.taxiapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TaxiBookingApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaxiBookingApplication.class, args);
    }
} 