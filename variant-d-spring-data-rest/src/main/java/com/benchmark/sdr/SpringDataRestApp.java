package com.benchmark.sdr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.benchmark.sdr")
public class SpringDataRestApp {
    public static void main(String[] args) {
        SpringApplication.run(SpringDataRestApp.class, args);
    }
}
