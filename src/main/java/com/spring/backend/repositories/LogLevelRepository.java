package com.spring.backend.repositories;

import com.spring.backend.models.LogLevel;
import com.spring.backend.models.enums.ELogLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LogLevelRepository extends JpaRepository<LogLevel, Long> {
    Optional<LogLevel> findByLogLevel(ELogLevel logLevel);
}
