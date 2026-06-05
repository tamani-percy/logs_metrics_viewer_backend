package com.spring.backend.repositories;

import com.spring.backend.dtos.projections.LogLevelCountProjection;
import com.spring.backend.models.LogLevel;
import com.spring.backend.models.PodLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PodLogRepository extends JpaRepository<PodLog, Long> {
    List<PodLog> findByNamespaceAndPodNameAndContainerNameAndTimestampBetweenOrderByTimestampDesc(
            String namespace, String podName, String containerName, LocalDateTime start, LocalDateTime end
    );

    @Query(value = "SELECT ll.log_level AS level, COUNT(pl.id) AS count " +
            "FROM pod_logs pl " +
            "JOIN pod_log_levels ll ON pl.log_level_id = ll.id " +
            "GROUP BY ll.log_level", nativeQuery = true)
    List<LogLevelCountProjection> countGroupedByLogLevel();

    Long countByLogLevel(LogLevel logLevel);


    @Query("SELECT DISTINCT p.containerName FROM PodLog p")
    List<String> getAllUniqueContainerNames();

    @Query("SELECT DISTINCT p.namespace FROM PodLog p")
    List<String> getAllUniqueNamespaces();
}


