package com.uptraceiq.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uptraceiq.model.Endpoint;
import com.uptraceiq.model.HealthCheckResult;
import com.uptraceiq.model.HealthStatus;
import com.uptraceiq.repository.HealthCheckResultRepository;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;

@Service
public class AlertService {

    private final LambdaClient lambdaClient;
    private final HealthCheckResultRepository resultRepository;
    private final ObjectMapper objectMapper;

    private static final String FUNCTION_NAME = "uptraceiq-alert-handler";

    // Spring injects the Lambda client (from LambdaConfig) and the repository
    public AlertService(LambdaClient lambdaClient, HealthCheckResultRepository resultRepository) {
        this.lambdaClient = lambdaClient;
        this.resultRepository = resultRepository;
        this.objectMapper = new ObjectMapper();
    }

    // called after every health check — only triggers Lambda on status transitions
    public void checkAndAlert(Endpoint endpoint, HealthStatus newStatus) {
        // find the previous result for this endpoint (skip the one we just saved)
        Optional<HealthCheckResult> previousResult = resultRepository
                .findTopByEndpointIdOrderByCheckedAtDesc(endpoint.getId());

        if (previousResult.isEmpty()) {
            return; // first check ever — nothing to compare against
        }

        HealthStatus previousStatus = previousResult.get().getStatus();

        // only alert when status actually changes
        boolean wentDown = previousStatus != HealthStatus.DOWN && newStatus == HealthStatus.DOWN;
        boolean recovered = previousStatus == HealthStatus.DOWN && newStatus != HealthStatus.DOWN;

        if (!wentDown && !recovered) {
            return; // no transition — don't spam alerts
        }

        try {
            String payload = objectMapper.writeValueAsString(new java.util.HashMap<String, String>() {{
                put("endpointName", endpoint.getName());
                put("endpointUrl", endpoint.getUrl());
                put("status", newStatus.name());
                put("checkedAt", LocalDateTime.now().toString());
                put("errorMessage", previousResult.get().getErrorMessage() != null
                        ? previousResult.get().getErrorMessage() : "");
            }});

            InvokeRequest request = InvokeRequest.builder()
                    .functionName(FUNCTION_NAME)
                    .payload(SdkBytes.fromUtf8String(payload))
                    .build();

            lambdaClient.invoke(request);

            System.out.println("Alert sent for " + endpoint.getName() + ": " + newStatus);

        } catch (Exception e) {
            System.out.println("Failed to send alert: " + e.getMessage());
        }
    }
}
