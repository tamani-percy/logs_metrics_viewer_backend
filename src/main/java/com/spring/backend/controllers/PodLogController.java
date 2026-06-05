package com.spring.backend.controllers;

import com.spring.backend.dtos.projections.LogLevelCountProjection;
import com.spring.backend.dtos.requests.PodLogNamespacePodContainerRangeRequest;
import com.spring.backend.models.PodLog;
import com.spring.backend.services.PodLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/logs")
@RestController
public class PodLogController {

    private final PodLogService podLogService;

    public PodLogController(PodLogService podLogService) {
        this.podLogService = podLogService;
    }

    @PostMapping("/namespace-pod-container-range")
    public ResponseEntity<List<PodLog>> getByNamespacePodContainerAndTimeRange(@RequestBody PodLogNamespacePodContainerRangeRequest podLogNamespacePodContainerRangeRequest) {
        return ResponseEntity.ok(podLogService.getByNamespacePodContainerAndTimeRange(podLogNamespacePodContainerRangeRequest));
    }

    @GetMapping("")
    public ResponseEntity<List<PodLog>> getAllLogs() {
        return ResponseEntity.ok(podLogService.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<PodLog> getPodLog(@PathVariable("id") Long id) {
        return ResponseEntity.ok(podLogService.getById(id));
    }

    @GetMapping("/namespaces")
    public ResponseEntity<List<String>> getAllUniqueNamespaces() {
        return ResponseEntity.ok(podLogService.getAllUniqueNamespaces());
    }

    @GetMapping("/containers")
    public ResponseEntity<List<String>> getAllUniqueContainerNames() {
        return ResponseEntity.ok(podLogService.getAllUniqueContainerNames());
    }

    // STATS
    @GetMapping("count-grouped-by-level")
    public ResponseEntity<List<LogLevelCountProjection>> countGroupedByLogLevel() {
        return ResponseEntity.ok(podLogService.countGroupedByLogLevel());
    }

    @GetMapping("count-by-log-level")
    public ResponseEntity<Long> countByLogLevel(@RequestParam("logLevel") String logLevel) {
        return ResponseEntity.ok(podLogService.countByLogLevel(logLevel));
    }
}
