package com.spring.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "pod_logs", indexes = {
        @Index(name = "idx_logs_lookup", columnList = "namespace, podName, containerName, timestamp")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PodLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime timestamp;
    private String namespace;
    private String podName;
    private String containerName;

    @ManyToOne
    @JoinColumn(name = "log_level_id", nullable = false)
    private LogLevel logLevel;

    @Column(columnDefinition = "TEXT")
    private String message;
}
