package com.digdes.java2023.repositories.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.digdes.java2023.repositories")
public class RepositoryConfig {
}
