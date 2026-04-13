// Central API client for the UptraceIQ backend.
// All fetch calls go through this module so the rest of the app only
// deals with typed data, not HTTP details.
//
// Requests use relative paths like '/api/endpoints'. In dev, Vite's proxy
// (see vite.config.ts) forwards them to http://localhost:8080. In production,
// the frontend will be served from the same origin as the backend, so the
// same relative paths will work unchanged.


import type {
    EndpointDTO,
    HealthCheckResultDTO,
    UptimeStatsDTO,
    CreateEndpointRequest,
} from '../types/api'

// Custom error class so callers can distinguis API errors from other errors

export class ApiError extends Error {
    constructor(public status: number, public statusText: string, message?: string) {
        super(message ?? `API error ${status}: ${statusText}`)
        this.name = 'ApiError'
    }
}

// Shared fetch helper. This throws ApiError on non-2xx, returns parsed JSON as T
async function apiRequest<T>(path: string, init?: RequestInit): Promise<T> {
    const response = await fetch(path, {
        ...init,
        headers: {
            'Content-Type': 'application/json',
            ...init?.headers,
        },
    })

    if (!response.ok) {
        throw new ApiError(response.status, response.statusText)
    }

    // 204 No Content, meaning no body to parse
    if (response.status === 204) {
        return undefined as T
    }

    return response.json() as Promise<T>
}


// Endpoints

export function fetchEndpoints(): Promise<EndpointDTO[]> {
    return apiRequest<EndpointDTO[]>('/api/endpoints')
}

export function fetchEndpoint(id: number): Promise<EndpointDTO> {
    return apiRequest<EndpointDTO>(`/api/endpoints/${id}`)
}

export function createEndpoint(body: CreateEndpointRequest): Promise<EndpointDTO> {
    return apiRequest<EndpointDTO>('/api/endpoints', {
        method: 'POST',
        body: JSON.stringify(body),
    })
}

export function deleteEndpoint(id: number): Promise<void> {
    return apiRequest<void>(`/api/endpoints/${id}`, {
        method: 'DELETE',
    })
}

// Health Check Results
export function fetchResults(
    endpointId: number,
    limit?: number,
): Promise<HealthCheckResultDTO[]> {
    const query = limit ? `?limit=${limit}` : ''
    return apiRequest<HealthCheckResultDTO[]>(`/api/endpoints/${endpointId}/results${query}`)
}

export function fetchUptime(
    endpointId: number,
    limit?: number,
): Promise<UptimeStatsDTO> {
    const query = limit ? `?limit=${limit}` : ''
    return apiRequest<UptimeStatsDTO>(`/api/endpoints/${endpointId}/uptime${query}`)
}
