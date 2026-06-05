package com.spring.backend.configs;

import com.spring.backend.models.LogLevel;
import com.spring.backend.models.enums.ELogLevel;
import com.spring.backend.repositories.LogLevelRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IntialiserConfig {

    @Bean
    public CommandLineRunner commandLineRunner(LogLevelRepository logLevelRepository) {
        return (args) -> {
            for (ELogLevel level : ELogLevel.values()) {
                if (logLevelRepository.findByLogLevel(level).isEmpty()) {
                    logLevelRepository.save(new LogLevel(null, level));
                }
            }
        };
    }
}
