package com.FletserTech.Fletgram.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:3000", "http://192.168.0.5:8082", "https://fletgram.loca.lt", "https://electric-polished-perch.ngrok-free.app");
    }
}

