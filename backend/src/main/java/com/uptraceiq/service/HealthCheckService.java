package com.uptraceiq.service;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.uptraceiq.model.Endpoint;
import com.uptraceiq.model.HealthCheckResult;
import com.uptraceiq.model.HealthStatus;
import com.uptraceiq.repository.EndpointRepository;
import com.uptraceiq.repository.HealthCheckResultRepository;

// Core service — pings all enabled endpoints in parallel every 30 seconds.
// Uses CompletableFuture (Java's version of Promise.all) so we check all
// endpoints at once instead of one at a time.
@Service
public class HealthCheckService {

    private final EndpointRepository endpointRepository;
    private final HealthCheckResultRepository resultRepository;
    private final HttpClient httpClient;

    // Spring injects the repositories automatically (dependency injection).
    // HttpClient is reusable and thread-safe — we only need one instance.
    public HealthCheckService(EndpointRepository endpointRepository,
                              HealthCheckResultRepository resultRepository) {

            this.endpointRepository = endpointRepository;
            this.resultRepository = resultRepository;
            this.httpClient = HttpClient.newBuilder()
                                .connectTimeout(Duration.ofSeconds(10))
                                .build();
    }

    // Runs every 30 seconds. @EnableScheduling in the main class activates this.
    @Scheduled(fixedRate = 30000)
    public void runHealthChecks() {
        List<Endpoint> endpoints = endpointRepository.findByEnabledTrue();

        if (endpoints.isEmpty()) {
            return;
        }

        // Kick off all checks in parallel — each one runs on its own thread.
        // Same idea as Promise.all(endpoints.map(ep => checkEndpoint(ep))) in JS.
        CompletableFuture<?>[] futures = endpoints.stream()
            .map(endpoint -> CompletableFuture.runAsync(() -> checkEndpoint(endpoint)))
            .toArray(CompletableFuture[]::new);

        // Wait for all checks to finish before the next cycle starts
        CompletableFuture.allOf(futures).join();
    }

    // Pings a single endpoint and saves the result to the DB.
    // Runs on a background thread via CompletableFuture.runAsync().
    private void checkEndpoint(Endpoint endpoint) {

        HealthCheckResult result = new HealthCheckResult();
        result.setEndpoint(endpoint);
        result.setCheckedAt(LocalDateTime.now());

        long startTime = System.currentTimeMillis();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint.getUrl()))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            long responseTime = System.currentTimeMillis() - startTime;

            result.setStatusCode(response.statusCode());
            result.setResponseTimeMs(responseTime);
            result.setStatus(determineStatus(response.statusCode(), responseTime));

        } catch (Exception e) {
            // Connection failed, timed out, DNS error, etc. — mark as DOWN
            long responseTime = System.currentTimeMillis() - startTime;

            result.setResponseTimeMs(responseTime);
            result.setStatus(HealthStatus.DOWN);
            result.setErrorMessage(e.getMessage());

        }

        resultRepository.save(result);
    }

    // Simple threshold logic — will be configurable per endpoint in Phase 8
    private HealthStatus determineStatus(int statusCode, long responseTimeMs) {
        if (statusCode >= 200 && statusCode < 300) {
            if (responseTimeMs > 5000) {
                return HealthStatus.DEGRADED;
            }
            return HealthStatus.UP;
        }
        return HealthStatus.DOWN;
    }

}
