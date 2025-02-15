package com.fajarsdk.ewallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class EwalletApplication {

	public static void main(String[] args) {
		SpringApplication.run(EwalletApplication.class, args);
	}

}
