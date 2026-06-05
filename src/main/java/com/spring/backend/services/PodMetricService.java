package com.spring.backend.services;

import com.spring.backend.dtos.requests.PodMetricNamespacePodRangeRequest;
import com.spring.backend.models.PodMetric;
import com.spring.backend.repositories.PodMetricRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PodMetricService {

    private final PodMetricRepository podMetricRepository;

    public PodMetricService(PodMetricRepository podMetricRepository) {
        this.podMetricRepository = podMetricRepository;
    }

    public List<PodMetric> getAll() {
        return podMetricRepository.findAll();
    }

    public PodMetric getById(Long id) {
        return podMetricRepository.findById(id).orElseThrow(() -> new RuntimeException("Unable to find metric with id " + id));
    }

    public List<PodMetric> getByNamespacePodAndTimeRange(PodMetricNamespacePodRangeRequest podMetricNamespacePodRangeRequest) {
        return podMetricRepository.findByNamespaceAndPodNameAndTimestampBetweenOrderByTimestampAsc(podMetricNamespacePodRangeRequest.getNamespace(), podMetricNamespacePodRangeRequest.getPodName(), podMetricNamespacePodRangeRequest.getStartTime(), podMetricNamespacePodRangeRequest.getEndTime());
    }

    public List<String> getAllUniquePodNames() {
        return podMetricRepository.getAllUniquePodNames();
    }

    // STATS
    public Double avgCpuByPod(String podName) {
        return podMetricRepository.avgCpuByPod(podName);
    }

    public Double avgMemoryByPod(String podName) {
        return podMetricRepository.avgMemoryByPod(podName);
    }

    public Long totalRequestsByPod(String podName) {
        return podMetricRepository.totalRequestsByPod(podName);
    }

    public Long totalErrorsByNamespace(String namespace) {
        return podMetricRepository.totalErrorsByNamespace(namespace);
    }

    public List<PodMetric> findCpuSpikes(Double threshold, LocalDateTime since) {
        return podMetricRepository.findCpuSpikes(threshold, since);
    }
}
