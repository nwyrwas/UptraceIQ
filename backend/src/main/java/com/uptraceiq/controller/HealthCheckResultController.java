package com.uptraceiq.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uptraceiq.dto.HealthCheckResultDTO;
import com.uptraceiq.dto.UptimeStatsDTO;
import com.uptraceiq.mapper.HealthCheckResultMapper;
import com.uptraceiq.model.HealthCheckResult;
import com.uptraceiq.model.HealthStatus;
import com.uptraceiq.repository.EndpointRepository;
import com.uptraceiq.repository.HealthCheckResultRepository;

@RestController
@RequestMapping("/api/endpoints")
public class HealthCheckResultController {

    private final HealthCheckResultRepository resultRepository;
    private final HealthCheckResultMapper resultMapper;
    private final EndpointRepository endpointRepository;

    public HealthCheckResultController(HealthCheckResultRepository resultRepository,
                                       HealthCheckResultMapper resultMapper,
                                       EndpointRepository endpointRepository) {
        this.resultRepository = resultRepository;
        this.resultMapper = resultMapper;
        this.endpointRepository = endpointRepository;
    }

    @GetMapping("/{id}/results")
    public ResponseEntity<List<HealthCheckResultDTO>> getResultsForEndpoint(
            @PathVariable Long id,
            @RequestParam(required = false) Integer limit) {

        if (!endpointRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        var stream = resultRepository
            .findByEndpointIdOrderByCheckedAtDesc(id)
            .stream();

        if (limit != null && limit > 0) {
            stream = stream.limit(limit);
        }

        List<HealthCheckResultDTO> results = stream
            .map(resultMapper::toDTO)
            .toList();

    return ResponseEntity.ok(results); 
    }

    @GetMapping("/{id}/uptime")
    public ResponseEntity<UptimeStatsDTO> getUptimeStats(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "100") Integer limit) {

        if (!endpointRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        List<HealthCheckResult> recentResults = resultRepository
            .findByEndpointIdOrderByCheckedAtDesc(id)
            .stream()
            .limit(limit)
            .toList();

        long totalChecks = recentResults.size();
        long upChecks = recentResults.stream()
            .filter(r -> r.getStatus() == HealthStatus.UP)
            .count();

        Double uptimePercentage = totalChecks == 0
            ? null
            : (upChecks * 100.0) / totalChecks;

        return ResponseEntity.ok(new UptimeStatsDTO(id, uptimePercentage, totalChecks, upChecks));
    }

}

