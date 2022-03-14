package com.cities.geo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class CitiesApplication {

	public static void main(String[] args) {
		SpringApplication.run(CitiesApplication.class, args);
	}

	@RequestMapping(value = "/Home")
	public String Home()
	{
		return "HOME GEO Cities";
	}
}
