package com.ocr.mediscreen_ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableFeignClients("com.ocr.mediscreen_ui")
@EnableSwagger2
public class Mediscreen_UIApplication {

	public static void main(String[] args) {
		SpringApplication.run(Mediscreen_UIApplication.class, args);
	}

}
