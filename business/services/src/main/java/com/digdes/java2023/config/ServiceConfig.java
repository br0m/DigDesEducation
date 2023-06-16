package com.digdes.java2023.config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.stringtemplate.v4.ST;

@Configuration
@ComponentScan({"com.digdes.java2023.services", "com.digdes.java2023.mapping"})
@PropertySource("classpath:application-service.properties")
@EnableAutoConfiguration
public class ServiceConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    ST messageTemplate() {
        return new ST("""
                Hello, <name>!
                You have been assigned a new task.
                
                Task details:
                Project: <codename>
                Title: <title>
                Description: <description>
                Deadline: <deadline>
                Status: <status>
                Author: <author>
                Creation date: <creationDate>
                """);
    }
}
