package com.acousea.backend.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AcouseaBackendApplication {

	public static void main(String[] args) {

		SpringApplication.run(AcouseaBackendApplication.class, args);
		System.out.println("Acousea backend application started!");
	}

}
