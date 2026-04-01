package com.uptraceiq.repository;


import com.uptraceiq.model.Endpoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


// Spring auto-implements this interface at runtime — we just define the method signatures.
// JpaRepository gives us save(), findById(), findAll(), deleteById(), count() for free.
public interface EndpointRepository extends JpaRepository<Endpoint, Long>  {

    // Spring generates the SQL from the method name:
    // findByEnabledTrue() → SELECT * FROM endpoints WHERE enabled = true
    // Used by HealthCheckService to only check active endpoints.
    List<Endpoint> findByEnabledTrue();

}
