package com.uptraceiq.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.uptraceiq.model.HealthCheckResult;
import com.uptraceiq.repository.HealthCheckResultRepository;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


@Service
public class MetricsArchiveService {
    private final HealthCheckResultRepository resultRepository;
    private final S3Client s3Client;
    private final ObjectMapper objectMapper;

    private static final String BUCKET_NAME = "uptraceiq-metrics-archive";

    // Spring injects the repository and S3 client automatically
    public MetricsArchiveService(HealthCheckResultRepository resultRepository, S3Client s3Client) {
        this.resultRepository = resultRepository;
        this.s3Client = s3Client;

        // configure Jackson to format JSON nicely instead of one long line
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    // runs once per day (86400000ms = 24 hours)
    @Scheduled(fixedRate = 86400000)
    public void archiveOldMetrics() {
        // anything older than 7 days gets archived
        LocalDateTime cutoff = LocalDateTime.now().minusDays(7);
        List<HealthCheckResult> oldResults = resultRepository.findByCheckedAtBefore(cutoff);

        if (oldResults.isEmpty()) {
            System.out.println("No old metrics to archive.");
            return;
        }

        try {
            // build the JSON payload — a list of maps, one per result
            List<Map<String, Object>> records = oldResults.stream().map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", result.getId());
                map.put("endpointId", result.getEndpoint().getId());
                map.put("statusCode", result.getStatusCode());
                map.put("responseTimeMs", result.getResponseTimeMs());
                map.put("status", result.getStatus().name());
                map.put("checkedAt", result.getCheckedAt().toString());
                map.put("errorMessage", result.getErrorMessage());
                return map;
            }).toList();

            String json = objectMapper.writeValueAsString(records);

            // file name includes the date so each archive is unique
            String key = "archives/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".json";

            // upload the JSON file to S3
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(key)
                    .contentType("application/json")
                    .build();

            s3Client.putObject(putRequest, RequestBody.fromString(json));

            // only delete from RDS after a successful upload
            resultRepository.deleteAll(oldResults);

            System.out.println("Archived " + oldResults.size() + " records to S3: " + key);

        } catch (Exception e) {
            System.out.println("Failed to archive metrics: " + e.getMessage());
        }
    }
}
