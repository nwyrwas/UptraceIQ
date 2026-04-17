package com.uptraceiq.model;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


// JPA entity — maps this class to the "endpoints" table in the database.
// Each row represents one service we're monitoring (URL, name, how often to check it).
@Entity
@Table(name = "endpoints")
public class Endpoint {

    // Primary key — auto-incremented by the database, we never set this manually.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Display name for the endpoint (e.g., "Google Homepage")
    @Column(nullable = false)
    private String name;

    // The URL that gets pinged by HealthCheckService (e.g., "https://google.com")
    @Column(nullable = false)
    private String url;

    // How often to check this endpoint. Defaults to 30s.
    // name = "..." maps Java camelCase to database snake_case
    @Column(name = "check_interval_seconds", nullable = false)
    private Integer checkIntervalSeconds = 30;

    // Lets us pause monitoring without deleting the endpoint
    @Column(nullable = false)
    private Boolean enabled = true;

    @Column(name = "response_time_threshold_ms", nullable = false)
    private Long responseTimeThresholdMs = 5000L;

    @Column(name = "failure_threshold", nullable = false)
    private Integer failureThreshold = 1;

    // Timestamps itself on creation
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();


    // JPA needs a no-arg constructor to create objects when reading from the DB
    public Endpoint() {}

    // Getters and Setters — JPA and Spring use these to read/write fields
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public Integer getCheckIntervalSeconds() { return checkIntervalSeconds; }
    public void setCheckIntervalSeconds(Integer checkIntervalSeconds) { this.checkIntervalSeconds = checkIntervalSeconds; }

    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Long getResponseTimeThresholdMs() { return responseTimeThresholdMs; }
    public void setResponseTimeThresholdMs(Long responseTimeThresholdMs) { this.responseTimeThresholdMs = responseTimeThresholdMs; }

    public Integer getFailureThreshold() { return failureThreshold; }
    public void setFailureThreshold(Integer failureThreshold) { this.failureThreshold = failureThreshold; }
}
