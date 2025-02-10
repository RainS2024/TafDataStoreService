package com.Tekarch.DataStoreService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DataStoreServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataStoreServiceApplication.class, args);
	}

}
