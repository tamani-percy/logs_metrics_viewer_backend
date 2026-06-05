package com.spring.backend.repositories;

import com.spring.backend.models.PodMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PodMetricRepository extends JpaRepository<PodMetric, Long> {
    List<PodMetric> findByNamespaceAndPodNameAndTimestampBetweenOrderByTimestampAsc(
            String namespace, String podName, LocalDateTime start, LocalDateTime end
    );

    @Query("SELECT AVG(p.cpuUsageCores) FROM PodMetric p WHERE p.podName = :podName")
    Double avgCpuByPod(@Param("podName") String podName);

    @Query("SELECT AVG(p.memoryUsageBytes) FROM PodMetric p WHERE p.podName = :podName")
    Double avgMemoryByPod(@Param("podName") String podName);

    @Query("SELECT SUM(p.requestCount) FROM PodMetric p WHERE p.podName = :podName")
    Long totalRequestsByPod(@Param("podName") String podName);

    @Query("SELECT SUM(p.errorCount) FROM PodMetric p WHERE p.namespace = :namespace")
    Long totalErrorsByNamespace(@Param("namespace") String namespace);

    @Query("SELECT p FROM PodMetric p WHERE p.cpuUsageCores > :threshold " +
            "AND p.timestamp >= :since")
    List<PodMetric> findCpuSpikes(
            @Param("threshold") Double threshold,
            @Param("since") LocalDateTime since
    );

    @Query("SELECT DISTINCT p.podName FROM PodMetric p")
    List<String> getAllUniquePodNames();

}
