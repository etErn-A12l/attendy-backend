package com.attendy.attendy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class AttendyApplication {

	public static void main(String[] args) {
		SpringApplication.run(AttendyApplication.class, args);
	}

}
