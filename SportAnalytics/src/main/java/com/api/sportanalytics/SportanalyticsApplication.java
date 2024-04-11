package com.api.sportanalytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.validation.annotation.Validated;

@SpringBootApplication
@Validated
public class SportanalyticsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SportanalyticsApplication.class, args);
	}

}
