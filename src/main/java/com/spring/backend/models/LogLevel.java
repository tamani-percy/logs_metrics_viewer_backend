package com.spring.backend.models;

import com.spring.backend.models.enums.ELogLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pod_log_levels")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ELogLevel logLevel;
}
