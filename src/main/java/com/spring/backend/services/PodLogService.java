package com.spring.backend.services;

import com.spring.backend.dtos.projections.LogLevelCountProjection;
import com.spring.backend.dtos.requests.PodLogNamespacePodContainerRangeRequest;
import com.spring.backend.models.LogLevel;
import com.spring.backend.models.PodLog;
import com.spring.backend.models.enums.ELogLevel;
import com.spring.backend.repositories.LogLevelRepository;
import com.spring.backend.repositories.PodLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PodLogService {

    private final PodLogRepository podLogRepository;
    private final LogLevelRepository logLevelRepository;

    public PodLogService(PodLogRepository podLogRepository, LogLevelRepository logLevelRepository) {
        this.podLogRepository = podLogRepository;
        this.logLevelRepository = logLevelRepository;
    }


    public List<PodLog> getAll() {
        return podLogRepository.findAll();
    }

    public PodLog getById(Long id) {
        return podLogRepository.findById(id).orElseThrow(() -> new RuntimeException("Unable to find log with id " + id));
    }

    public List<PodLog> getByNamespacePodContainerAndTimeRange(PodLogNamespacePodContainerRangeRequest podLogNamespacePodContainerRangeRequest) {
        return podLogRepository.findByNamespaceAndPodNameAndContainerNameAndTimestampBetweenOrderByTimestampDesc(podLogNamespacePodContainerRangeRequest.getNamespace(), podLogNamespacePodContainerRangeRequest.getPodName(), podLogNamespacePodContainerRangeRequest.getContainerName(), podLogNamespacePodContainerRangeRequest.getStartTime(), podLogNamespacePodContainerRangeRequest.getEndTime());
    }

    public List<String> getAllUniqueContainerNames() {
        return podLogRepository.getAllUniqueContainerNames();
    }

    public List<String> getAllUniqueNamespaces() {
        return podLogRepository.getAllUniqueNamespaces();
    }

    // STATS
    public List<LogLevelCountProjection> countGroupedByLogLevel() {
        return podLogRepository.countGroupedByLogLevel();
    }

    public Long countByLogLevel(String logLevel) {
        LogLevel existingLogLevel = logLevelRepository.findByLogLevel(ELogLevel.valueOf(logLevel)).orElseThrow(() -> new RuntimeException("Unable to find log level " + logLevel));
        return podLogRepository.countByLogLevel(existingLogLevel);
    }
}
