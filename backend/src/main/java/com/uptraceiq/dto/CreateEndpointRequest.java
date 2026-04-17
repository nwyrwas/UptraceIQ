package com.uptraceiq.dto;

public class CreateEndpointRequest {
    
    private String name;
    private String url;
    private Integer checkIntervalSeconds;
    private Boolean enabled;
    private Long responseTimeThresholdMs;
    private Integer failureThreshold;

    public CreateEndpointRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public Integer getCheckIntervalSeconds() { return checkIntervalSeconds; }
    public void setCheckIntervalSeconds(Integer checkIntervalSeconds) { this.checkIntervalSeconds = checkIntervalSeconds; }

    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }

    public Long getResponseTimeThresholdMs() { return responseTimeThresholdMs; }
    public void setResponseTimeThresholdMs(Long responseTimeThresholdMs) { this.responseTimeThresholdMs = responseTimeThresholdMs; }

    public Integer getFailureThreshold() { return failureThreshold; }
    public void setFailureThreshold(Integer failureThreshold) { this.failureThreshold = failureThreshold; }
}
