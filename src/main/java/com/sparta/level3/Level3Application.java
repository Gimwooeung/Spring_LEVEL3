package com.sparta.level3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Level3Application {

	public static void main(String[] args) {
		SpringApplication.run(Level3Application.class, args);
	}

}
