package com.spring.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "pod_metrics", indexes = {
        @Index(name = "idx_metrics_lookup", columnList = "namespace, podName, timestamp")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PodMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;
    private String namespace;
    private String podName;
    private Double cpuUsageCores;
    private Long memoryUsageBytes;
    private Integer requestCount;
    private Integer errorCount;
}
