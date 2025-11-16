package com.ytload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class YtLoadApplication {

	public static void main(String[] args) {
		SpringApplication.run(YtLoadApplication.class, args);
	}

}
