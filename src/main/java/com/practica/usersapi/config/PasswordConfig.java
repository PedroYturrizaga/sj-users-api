package com.practica.usersapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.regex.Pattern;

@Configuration
public class PasswordConfig {

    @Bean
    public Pattern passwordPattern() {
        //  Configurable password regex (example: at least 8 chars, 1 uppercase, 1 lowercase, 1 digit)
        return Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");
    }
}