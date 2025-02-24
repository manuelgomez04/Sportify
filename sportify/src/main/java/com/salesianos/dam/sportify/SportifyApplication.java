package com.salesianos.dam.sportify;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class SportifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SportifyApplication.class, args);
	}

}
