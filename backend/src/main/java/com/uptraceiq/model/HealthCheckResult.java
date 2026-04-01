package com.uptraceiq.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// JPA entity — each row is one health check result (one ping of one endpoint).
// Many results belong to one endpoint (Many-to-One relationship).
@Entity
@Table(name = "health_check_results")
public class HealthCheckResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Links this result to the endpoint it checked.
    // LAZY = don't load the full Endpoint object until we actually need it (saves memory).
    // @JoinColumn creates the endpoint_id foreign key in this table.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "endpoint_id", nullable = false)
    private Endpoint endpoint;

    // Nullable — if the connection times out, there's no status code to record
    @Column(name = "status_code")
    private Integer statusCode;

    // How long the request took. Used for charts and DEGRADED detection (>5s)
    @Column(name = "response_time_ms")
    private Long responseTimeMs;

    // UP, DOWN, or DEGRADED — stored as a string in the DB, not a number
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HealthStatus status;

    @Column(name = "checked_at", nullable = false)
    private LocalDateTime checkedAt = LocalDateTime.now();

    // Nullable — only populated when the check fails (e.g., "Connection timed out")
    @Column(name = "error_message")
    private String errorMessage;


    public HealthCheckResult() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Endpoint getEndpoint() { return endpoint; }
    public void setEndpoint(Endpoint endpoint) { this.endpoint = endpoint; }

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