package com.uptraceiq.dto;

import java.time.LocalDateTime;
import com.uptraceiq.model.HealthStatus;

public class HealthCheckResultDTO {
    private Long id;
    private Long endpointId;
    private Integer statusCode;
    private Long responseTimeMs;
    private HealthStatus status;
    private LocalDateTime checkedAt;
    private String errorMessage;



    public HealthCheckResultDTO(){}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEndpointId() { return endpointId; }
    public void setEndpointId(Long endpointId) { this.endpointId = endpointId; }

    public Integer getStatusCode() { return statusCode; }
    public void setStatusCode(Integer statusCode) { this.statusCode = statusCode; }

    public Long getResponseTimeMs() { return responseTimeMs; }
    public void setResponseTimeMs(Long responseTimeMs) { this.responseTimeMs = responseTimeMs; }

    public HealthStatus getStatus() { return status; }
    public void setStatus(HealthStatus status) { this.status = status; }

    public LocalDateTime getCheckedAt() { return checkedAt; }
    public void setCheckedAt(LocalDateTime checkedAt) { this.checkedAt = checkedAt; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
