package com.digdes.java2023.main;

import com.digdes.java2023.config.ServiceConfig;
import com.digdes.java2023.controllers.config.ControllerConfig;
import com.digdes.java2023.dto.config.DtoConfig;
import com.digdes.java2023.main.security.SecurityConfig;
import com.digdes.java2023.model.config.ModelConfig;
import com.digdes.java2023.repositories.config.RepositoryConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


@SpringBootApplication
@Import({ModelConfig.class, RepositoryConfig.class, ServiceConfig.class, SecurityConfig.class, ControllerConfig.class, DtoConfig.class})
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
