package com.lits.tovisitapp;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ToVisitApplication {

	public static void main(String[] args) {
		SpringApplication.run(com.lits.tovisitapp.ToVisitApplication.class, args);
	}

	@Bean
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}

	@Bean
	public BCryptPasswordEncoder geBbCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
