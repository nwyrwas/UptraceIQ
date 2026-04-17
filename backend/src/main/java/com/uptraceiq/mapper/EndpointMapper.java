package com.uptraceiq.mapper;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.uptraceiq.dto.CreateEndpointRequest;
import com.uptraceiq.dto.EndpointDTO;
import com.uptraceiq.model.Endpoint;
import com.uptraceiq.model.HealthCheckResult;
import com.uptraceiq.repository.HealthCheckResultRepository;

@Component
public class EndpointMapper {
    
    private final HealthCheckResultRepository resultRepository;

    public EndpointMapper(HealthCheckResultRepository resultRepository) { this.resultRepository = resultRepository; }

    public EndpointDTO toDTO(Endpoint entity) {
        EndpointDTO dto = new EndpointDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setUrl(entity.getUrl());
        dto.setCheckIntervalSeconds(entity.getCheckIntervalSeconds());
        dto.setEnabled(entity.getEnabled());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setResponseTimeThresholdMs(entity.getResponseTimeThresholdMs());
        dto.setFailureThreshold(entity.getFailureThreshold());

        Optional<HealthCheckResult> latestResult = 
            resultRepository.findTopByEndpointIdOrderByCheckedAtDesc(entity.getId());

        latestResult.ifPresent(result -> {
            dto.setCurrentStatus(result.getStatus());
            dto.setLastCheckedAt(result.getCheckedAt());
        });

        return dto;
    }

    public Endpoint toEntity(CreateEndpointRequest request) {
        Endpoint entity = new Endpoint();
        entity.setName(request.getName());
        entity.setUrl(request.getUrl());

        entity.setCheckIntervalSeconds(
            Optional.ofNullable(request.getCheckIntervalSeconds()).orElse(30)
        );

        entity.setEnabled(
            Optional.ofNullable(request.getEnabled()).orElse(true)
        );

        entity.setResponseTimeThresholdMs(
            Optional.ofNullable(request.getResponseTimeThresholdMs()).orElse(5000L)
        );
        entity.setFailureThreshold(
            Optional.ofNullable(request.getFailureThreshold()).orElse(1)
        );

        entity.setCreatedAt(LocalDateTime.now());

        return entity;
    }
}
