package com.spring.backend.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PodMetricNamespacePodRangeRequest {
    private String namespace;
    private String podName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
