package com.benchmark.mvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.benchmark.mvc")
public class SpringMvcApp {
    public static void main(String[] args) {
        SpringApplication.run(SpringMvcApp.class, args);
    }
}
