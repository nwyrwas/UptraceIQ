// TypeScript types matching the backend DTOs in com.uptraceiq.dto
// Keep these in sync with the Java DTOs whenever the backend changes

export type HealthStatus = 'UP' | 'DOWN' | 'DEGRADED'

export interface EndpointDTO {
    id: number
    name: string
    url: string
    checkIntervalSeconds: number
    enabled: boolean
    createdAt: string
    currentStatus: HealthStatus | null
    lastCheckedAt: string | null
}

export interface HealthCheckResultDTO {
    id: number
    endpointId: number
    statusCode: number | null
    responseTimeMs: number | null
    status: HealthStatus
    checkedAt: string
    errorMessage: string | null
}

export interface UptimeStatsDTO {
    endpointId: number
    uptimePercentage: number | null
    totalChecks: number
    upChecks: number
}

export interface CreateEndpointRequest {
    name: string
    url: string
    checkIntervalSeconds?: number
    enabled?: boolean
}