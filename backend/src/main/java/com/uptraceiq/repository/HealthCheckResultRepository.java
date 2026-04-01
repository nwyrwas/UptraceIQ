package com.uptraceiq.repository;

import com.uptraceiq.model.HealthCheckResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Same pattern as EndpointRepository — Spring auto-implements the interface.
public interface HealthCheckResultRepository extends JpaRepository<HealthCheckResult, Long> {

    // Derived query — Spring parses the method name into SQL:
    // SELECT * FROM health_check_results WHERE endpoint_id = ? ORDER BY checked_at DESC
    // Gets all results for one endpoint, newest first. Used for the dashboard timeline.
    List<HealthCheckResult> findByEndpointIdOrderByCheckedAtDesc(Long endpointId);

}
