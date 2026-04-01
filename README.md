# UptraceIQ

A system health and uptime monitoring platform that tracks endpoint availability, response times, and incidents in real time.

## Tech Stack

| Layer | Technology |
|-------|-----------|
| **Frontend** | React 18, Tailwind CSS |
| **Backend** | Java 17, Spring Boot 3 |
| **Database** | AWS RDS PostgreSQL |
| **Storage** | AWS S3 (metrics archiving) |
| **Alerts** | AWS Lambda + SNS |
| **Monitoring** | AWS CloudWatch |

## Architecture

The Java backend pings configurable endpoints on a schedule using `CompletableFuture` for parallel HTTP requests. Results (response time, status code, availability) are stored in RDS PostgreSQL. AWS Lambda processes metric batches and triggers alerts via SNS when services go down or response times spike.

The React dashboard displays uptime percentages, response time charts, incident history, and live status indicators.

## Project Phases

- [x] **Phase 1** — Project setup (Spring Boot, Maven, H2 dev database)
- [ ] **Phase 2** — Health check domain model and JPA entities
- [ ] **Phase 3** — RDS metrics schema and persistence
- [ ] **Phase 4** — S3 metrics archiving
- [ ] **Phase 5** — Lambda alert processor with SNS notifications
- [ ] **Phase 6** — Spring Boot REST API
- [ ] **Phase 7** — React dashboard (uptime charts, incident feed, status badges)
- [ ] **Phase 8** — Configurable alert thresholds per endpoint

## Screenshots

### Phase 1 — Project Setup
![H2 Console](docs/screenshots/phase-1/h2-console.jpg)
*H2 in-memory database console — used for local development before connecting to AWS RDS PostgreSQL.*

![Actuator Health](docs/screenshots/phase-1/actuator-health.jpg)
*Spring Boot Actuator health endpoint — returns service status. AWS load balancers will use this to verify our backend is alive.*



## Running Locally

### Prerequisites
- Java 17+
- Maven 3.9+

### Backend
```bash
cd backend
./mvnw spring-boot:run
