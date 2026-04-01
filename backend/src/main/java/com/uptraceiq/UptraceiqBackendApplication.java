package com.uptraceiq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

// @SpringBootApplication — auto-configures everything based on our dependencies.
// @EnableScheduling — required for @Scheduled to work in HealthCheckService.
@SpringBootApplication
@EnableScheduling
public class UptraceiqBackendApplication {

	// Entry point — boots up the embedded Tomcat server, DB connection pool, and scheduler.
	public static void main(String[] args) {
		SpringApplication.run(UptraceiqBackendApplication.class, args);
	}

}
