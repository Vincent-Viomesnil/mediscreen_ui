package com.ocr.mediscreen_ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.ocr.mediscreen_ui")
public class Mediscreen_UIApplication {

	public static void main(String[] args) {
		SpringApplication.run(Mediscreen_UIApplication.class, args);
	}

}
