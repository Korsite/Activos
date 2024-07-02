package com.example.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example"})
public class ActivosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActivosApplication.class, args);
	}

}
