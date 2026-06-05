package com.spring.backend.controllers;

import com.spring.backend.dtos.requests.PodMetricNamespacePodRangeRequest;
import com.spring.backend.models.PodMetric;
import com.spring.backend.services.PodMetricService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/api/v1/metrics")
@RestController
public class PodMetricController {

    private final PodMetricService podMetricService;

    public PodMetricController(PodMetricService podMetricService) {
        this.podMetricService = podMetricService;
    }

    @PostMapping("/namespace-pod-range")
    public ResponseEntity<List<PodMetric>> getByNamespacePodAndTimeRange(@RequestBody PodMetricNamespacePodRangeRequest podMetricNamespacePodRangeRequest) {
        return ResponseEntity.ok(podMetricService.getByNamespacePodAndTimeRange(podMetricNamespacePodRangeRequest));
    }

    @GetMapping("/pods")
    public ResponseEntity<List<String>> getAllUniquePodNames() {
        return ResponseEntity.ok(podMetricService.getAllUniquePodNames());
    }

    @GetMapping("{id}")
    public ResponseEntity<PodMetric> getMetricById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(podMetricService.getById(id));
    }

    @GetMapping("")
    public ResponseEntity<List<PodMetric>> getAllMetrics() {
        return ResponseEntity.ok(podMetricService.getAll());
    }

    // STATS
    @GetMapping("/avg-cpu-by-pod")
    public ResponseEntity<Double> getAverageCpuByPod(@RequestParam("podName") String podName) {
        return ResponseEntity.ok(podMetricService.avgCpuByPod(podName));
    }

    @GetMapping("/avg-memory-by-pod")
    public ResponseEntity<Double> getAverageMemoryByPod(@RequestParam("podName") String podName) {
        return ResponseEntity.ok(podMetricService.avgMemoryByPod(podName));
    }

    @GetMapping("/total-requests-by-pod")
    public ResponseEntity<Long> totalRequestsByPod(@RequestParam("podName") String podName) {
        return ResponseEntity.ok(podMetricService.totalRequestsByPod(podName));
    }

    @GetMapping("/total-errors-by-namespace")
    public ResponseEntity<Long> totalErrorsByNamespace(@RequestParam("namespace") String namespace) {
        return ResponseEntity.ok(podMetricService.totalErrorsByNamespace(namespace));
    }

    @GetMapping("/cpu-spikes")
    public ResponseEntity<List<PodMetric>> findCpuSpikes(@RequestParam("threshold") Double threshold, @RequestParam("since") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since) {
        return ResponseEntity.ok(podMetricService.findCpuSpikes(threshold, since));
    }
}
