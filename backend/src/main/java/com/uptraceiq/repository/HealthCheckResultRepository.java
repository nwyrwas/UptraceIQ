package com.uptraceiq.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uptraceiq.model.HealthCheckResult;

// Same pattern as EndpointRepository — Spring auto-implements the interface.
public interface HealthCheckResultRepository extends JpaRepository<HealthCheckResult, Long> {

    // Derived query — Spring parses the method name into SQL:
    // SELECT * FROM health_check_results WHERE endpoint_id = ? ORDER BY checked_at DESC
    // Gets all results for one endpoint, newest first. Used for the dashboard timeline.
    List<HealthCheckResult> findByEndpointIdOrderByCheckedAtDesc(Long endpointId);

    // Finds all health check results recorded before a given timestamp.
    // Used by the archiver to grab anything older than 7 days for S3 export.
    List<HealthCheckResult> findByCheckedAtBefore(java.time.LocalDateTime cutoff);
}
