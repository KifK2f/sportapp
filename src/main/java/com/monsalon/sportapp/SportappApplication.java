package com.monsalon.sportapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SportappApplication {

	public static void main(String[] args) {
		SpringApplication.run(SportappApplication.class, args);
		System.out.println("Sportapp is running!");
	}
}
