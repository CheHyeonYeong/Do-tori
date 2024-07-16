package com.dotori.dotori;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DotoriApplication {

	public static void main(String[] args) {
		SpringApplication.run(DotoriApplication.class, args);
	}

}
