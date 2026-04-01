package com.uptraceiq.model;

// The three possible outcomes of a health check.
// Using an enum instead of strings to avoid typo bugs ("up" vs "Up" vs "UP").
// Stored as text in the DB via @Enumerated(EnumType.STRING) in HealthCheckResult.
public enum HealthStatus {
    UP,         // 2xx response, under 5 seconds
    DOWN,       // non-2xx, timeout, or connection failure
    DEGRADED    // 2xx response, but took over 5 seconds
}