package com.uptraceiq.mapper;

import org.springframework.stereotype.Component;

import com.uptraceiq.dto.HealthCheckResultDTO;
import com.uptraceiq.model.HealthCheckResult;

@Component
public class HealthCheckResultMapper {
    
    public HealthCheckResultDTO toDTO(HealthCheckResult entity) {
        HealthCheckResultDTO dto = new HealthCheckResultDTO();

        dto.setId(entity.getId());
        dto.setEndpointId(entity.getEndpoint().getId());
        dto.setStatusCode(entity.getStatusCode());
        dto.setResponseTimeMs(entity.getResponseTimeMs());
        dto.setStatus(entity.getStatus());
        dto.setCheckedAt(entity.getCheckedAt());
        dto.setErrorMessage(entity.getErrorMessage());

        return dto;

    }
}
