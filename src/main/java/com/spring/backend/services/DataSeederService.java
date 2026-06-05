package com.spring.backend.services;

import com.spring.backend.models.PodLog;
import com.spring.backend.models.PodMetric;
import com.spring.backend.models.enums.ELogLevel;
import com.spring.backend.repositories.LogLevelRepository;
import com.spring.backend.repositories.PodLogRepository;
import com.spring.backend.repositories.PodMetricRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class DataSeederService {

    private final PodLogRepository podLogRepository;
    private final PodMetricRepository podMetricRepository;

    private final LogLevelRepository logLevelRepository;
    private boolean seedCompleted = false;

    public DataSeederService(PodLogRepository podLogRepository, PodMetricRepository podMetricRepository, LogLevelRepository logLevelRepository) {
        this.podLogRepository = podLogRepository;
        this.podMetricRepository = podMetricRepository;
        this.logLevelRepository = logLevelRepository;
    }

    @Scheduled(fixedRate = 3000, initialDelay = 30000)
    public void generateLiveTicks() {
        if (!seedCompleted) {
            return;
        }

        generateRandomData(LocalDateTime.now());
    }

    @EventListener(ApplicationReadyEvent.class)
    public void seedData() {
        LocalDateTime now = LocalDateTime.now();

        for (int i = 120; i >= 0; i--) {
            LocalDateTime targetTime = now.minusMinutes(i);
            generateRandomData(targetTime);
        }

        seedCompleted = true;
        System.out.println(">>> Demo data seeded successfully for the past 2 hours.");
    }

    private static final String[] NAMESPACES = {
            "production", "staging"
    };

    private static final String[][] PODS_BY_NAMESPACE = {
            {"api_gateway-3301", "payment_service-4401"},
            {"api_gateway-0021", "payment_service-0041"}
    };

    private static final String[][] CONTAINERS_BY_POD_PREFIX = {
            {"api_gateway_container", "envoy_proxy"},
            {"payment_container", "vault_agent"}
    };

    private static final String[] INFO_MESSAGES = {
            "Successfully processed request in %dms | Status 200",
            "Health check passed | Uptime: %dh | Memory stable"
    };

    private static final String[] WARN_MESSAGES = {
            "Database connection pool utilization at %d%% | Threshold: 80%%",
            "Response time degradation detected: avg %dms over last 60s"
    };

    private static final String[] ERROR_MESSAGES = {
            "Database connection timeout after %dms | Pool exhausted",
            "Downstream service payment_service returned HTTP 503 | Retry budget exceeded"
    };

    private void generateRandomData(LocalDateTime time) {
        Random random = ThreadLocalRandom.current();

        long secondsOffset = random.nextLong(
                -30L * 24 * 60 * 60,
                30L * 24 * 60 * 60 + 1
        );
        time = time.plusSeconds(secondsOffset);

        int nsIndex = random.nextInt(NAMESPACES.length);
        String namespace = NAMESPACES[nsIndex];

        String[] nsPods = PODS_BY_NAMESPACE[nsIndex];
        String pod = nsPods[random.nextInt(nsPods.length)];

        String[] containers = resolveContainers(pod);
        String container = containers[random.nextInt(containers.length)];

        PodMetric podMetric = new PodMetric();
        podMetric.setNamespace(namespace);
        podMetric.setTimestamp(time);
        podMetric.setPodName(pod);
        podMetric.setCpuUsageCores(0.05 + (0.9 * random.nextDouble()));
        podMetric.setMemoryUsageBytes(256L * 1024 * 1024 + random.nextInt(512 * 1024 * 1024));
        podMetric.setRequestCount(random.nextInt(300));
        podMetric.setErrorCount(random.nextInt(10) >= 8 ? random.nextInt(5) : 0);
        podMetricRepository.save(podMetric);

        int numberOfLogsToRandomlyCreate = random.nextInt(6);
        for (int i = 0; i < numberOfLogsToRandomlyCreate; i++) {
            PodLog podLog = new PodLog();
            podLog.setTimestamp(time.plusNanos(random.nextInt(1_000_000)));
            podLog.setNamespace(namespace);
            podLog.setPodName(pod);
            podLog.setContainerName(container);

            int roll = random.nextInt(100);
            if (roll < 50) {
                podLog.setLogLevel(logLevelRepository.findByLogLevel(ELogLevel.INFO)
                        .orElseThrow(() -> new RuntimeException("INFO level not found")));
                podLog.setMessage(randomInfoMessage());
            } else if (roll < 75) {
                podLog.setLogLevel(logLevelRepository.findByLogLevel(ELogLevel.WARN)
                        .orElseThrow(() -> new RuntimeException("WARN level not found")));
                podLog.setMessage(randomWarnMessage());
            } else {
                podLog.setLogLevel(logLevelRepository.findByLogLevel(ELogLevel.ERROR)
                        .orElseThrow(() -> new RuntimeException("ERROR level not found")));
                podLog.setMessage(randomErrorMessage());
            }

            podLogRepository.save(podLog);
        }
    }

    private String[] resolveContainers(String pod) {
        if (pod.startsWith("api_gateway")) return CONTAINERS_BY_POD_PREFIX[0];
        return CONTAINERS_BY_POD_PREFIX[1]; // payment
    }

    private String randomInfoMessage() {
        Random r = ThreadLocalRandom.current();
        return r.nextBoolean()
                ? String.format(INFO_MESSAGES[0], r.nextInt(200))
                : String.format(INFO_MESSAGES[1], r.nextInt(720));
    }

    private String randomWarnMessage() {
        Random r = ThreadLocalRandom.current();
        return r.nextBoolean()
                ? String.format(WARN_MESSAGES[0], 75 + r.nextInt(20))
                : String.format(WARN_MESSAGES[1], 500 + r.nextInt(4500));
    }

    private String randomErrorMessage() {
        Random r = ThreadLocalRandom.current();
        return r.nextBoolean()
                ? String.format(ERROR_MESSAGES[0], 3000 + r.nextInt(27000))
                : ERROR_MESSAGES[1];
    }
}
