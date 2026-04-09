package com.uptraceiq.dto;

public class UptimeStatsDTO {
    private Long endpointId;
    private Double uptimePercentage;
    private Long totalChecks;
    private Long upChecks;

    public UptimeStatsDTO() {}

    public UptimeStatsDTO(Long endpointId, Double uptimePercentage, Long totalChecks, Long upChecks) {
        this.endpointId = endpointId;
        this.uptimePercentage = uptimePercentage;
        this.totalChecks = totalChecks;
        this.upChecks = upChecks;
    }

    public Long getEndpointId() { return endpointId; }
    public void setEndpointId(Long endpointId) { this.endpointId = endpointId; }

    public Double getUptimePercentage() { return uptimePercentage; }
    public void setUptimePercentage(Double uptimePercentage) { this.uptimePercentage = uptimePercentage; }

    public Long getTotalChecks() { return totalChecks; }
    public void setTotalChecks(Long totalChecks) { this.totalChecks = totalChecks; }

    public Long getUpChecks() { return upChecks; }
    public void setUpChecks(Long upChecks) { this.upChecks = upChecks; }
}
