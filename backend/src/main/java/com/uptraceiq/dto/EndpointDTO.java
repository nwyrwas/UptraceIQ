package com.uptraceiq.dto;

import java.time.LocalDateTime;

import com.uptraceiq.model.HealthStatus;

public class EndpointDTO {
    private Long id;
    private String name;
    private String url;
    private Integer checkIntervalSeconds;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private HealthStatus currentStatus;
    private LocalDateTime lastCheckedAt;
    private Long responseTimeThresholdMs;
    private Integer failureThreshold;

    public EndpointDTO() {
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getCheckIntervalSeconds() {
        return checkIntervalSeconds;
    }
    public void setCheckIntervalSeconds(Integer checkIntervalSeconds){
        this.checkIntervalSeconds = checkIntervalSeconds;
    }

    public Boolean getEnabled() {
        return enabled;
    }
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public HealthStatus getCurrentStatus() {
        return currentStatus;
    }
    public void setCurrentStatus(HealthStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public LocalDateTime getLastCheckedAt() {
        return lastCheckedAt;
    }
    public void setLastCheckedAt(LocalDateTime lastCheckedAt) {
        this.lastCheckedAt = lastCheckedAt;
    }
    public Long getResponseTimeThresholdMs() { return responseTimeThresholdMs; }
    public void setResponseTimeThresholdMs(Long responseTimeThresholdMs) { this.responseTimeThresholdMs = responseTimeThresholdMs; }

    public Integer getFailureThreshold() { return failureThreshold; }
    public void setFailureThreshold(Integer failureThreshold) { this.failureThreshold = failureThreshold; }
}
