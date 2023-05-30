package com.digdes.java2023.model.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan("com.digdes.java2023.model")
public class ModelConfig {
}
