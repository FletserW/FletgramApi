package com.FletserTech.Fletgram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@ComponentScan(basePackages = "com.FletserTech.Fletgram")
@OpenAPIDefinition(
	info = @Info(
		title = "Fletgram API",
		version = "1.1",
		description = "Api para rede social.",
		contact = @Contact(name = "FletserW", email = "nicolas.s.borba1@gmail.com", url = "https://github.com/FletserW")
	)
)
public class FletgramApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(FletgramApplication.class, args);
	}

}
