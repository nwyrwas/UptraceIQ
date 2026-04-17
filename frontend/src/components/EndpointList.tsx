import { useState } from 'react'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { deleteEndpoint, fetchEndpoints } from '../api/client'
import type { EndpointDTO, HealthStatus } from '../types/api'
import { EndpointDetail } from './EndpointDetail'
import { EndpointSparkline } from './EndpointSparkline'


const STATUS_BADGE_BASE =
  'inline-block px-3 py-1 rounded-full text-xs font-semibold uppercase tracking-wider whitespace-nowrap'

const STATUS_BADGE_VARIANTS: Record<HealthStatus | 'UNKNOWN', string> = {
  UP: 'bg-green-100 text-green-800 dark:bg-green-950/50 dark:text-green-300',
  DOWN: 'bg-red-100 text-red-800 dark:bg-red-950/50 dark:text-red-300',
  DEGRADED: 'bg-amber-100 text-amber-800 dark:bg-amber-950/50 dark:text-amber-300',
  UNKNOWN: 'bg-gray-200 text-gray-600 dark:bg-slate-700 dark:text-slate-300',
}

function statusClassName(status: HealthStatus | null): string {
  return `${STATUS_BADGE_BASE} ${STATUS_BADGE_VARIANTS[status ?? 'UNKNOWN']}`
}


function statusLabel(status: HealthStatus | null): string {
  return status ?? 'UNKNOWN'
}

function formatLastChecked(timestamp: string | null): string {
  if (!timestamp) return 'Never'
  const diffSec = Math.floor((Date.now() - new Date(timestamp).getTime()) / 1000)
  if (diffSec < 5) return 'just now'
  if (diffSec < 60) return `${diffSec}s ago`
  const diffMin = Math.floor(diffSec / 60)
  if (diffMin < 60) return `${diffMin}m ago`
  const diffHr = Math.floor(diffMin / 60)
  if (diffHr < 24) return `${diffHr}h ago`
  const diffDay = Math.floor(diffHr / 24)
  return `${diffDay}d ago`
}

export function EndpointList() {
  const { data: endpoints, isLoading, error } = useQuery({
    queryKey: ['endpoints'],
    queryFn: fetchEndpoints,
  })

  const queryClient = useQueryClient()

  const [expandedId, setExpandedId] = useState<number | null>(null)

  function toggleExpanded(id: number) {
    setExpandedId((current) => (current === id ? null : id))
  }

  const deleteMutation = useMutation({
    mutationFn: (id: number) => deleteEndpoint(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['endpoints'] })
    },
  })

  function handleDelete(endpoint: EndpointDTO) {
    const confirmed = window.confirm(
      `Delete "${endpoint.name}"? This cannot be undone.`,
    )
    if (!confirmed) return
    deleteMutation.mutate(endpoint.id)
  }

  if (isLoading) {
    return (
      <div className="flex flex-col gap-4">
        {[1, 2, 3].map((i) => (
          <div
            key={i}
            className="bg-white dark:bg-slate-800 border border-gray-200 dark:border-slate-700 rounded-lg px-6 py-5 animate-pulse"
          >
            <div className="flex justify-between items-center mb-3">
              <div className="h-5 bg-gray-200 dark:bg-slate-700 rounded w-1/3"></div>
              <div className="h-6 bg-gray-200 dark:bg-slate-700 rounded-full w-16"></div>
            </div>
            <div className="h-4 bg-gray-200 dark:bg-slate-700 rounded w-2/3 mb-3"></div>
            <div className="flex justify-between">
              <div className="h-3 bg-gray-200 dark:bg-slate-700 rounded w-1/4"></div>
              <div className="h-3 bg-gray-200 dark:bg-slate-700 rounded w-1/3"></div>
            </div>
          </div>
        ))}
      </div>
    )
  }

  if (error) {
    return (
      <p className="text-center text-red-600 dark:text-red-400 p-8">
        Failed to load endpoints: {error.message}
      </p>
    )
  }

  if (!endpoints || endpoints.length === 0) {
    return <p className="text-center text-gray-500 dark:text-slate-400 p-8">No endpoints yet.</p>
  }

  return (
    <ul className="list-none p-0 m-0 flex flex-col gap-4">
      {endpoints.map((endpoint: EndpointDTO) => (
        <li
          key={endpoint.id}
          className={`bg-white dark:bg-slate-800 border rounded-lg px-6 py-5 shadow-sm transition-all duration-150 cursor-pointer hover:shadow-md hover:-translate-y-0.5 ${
            expandedId === endpoint.id
              ? 'border-blue-500 ring-2 ring-blue-500/20'
              : 'border-gray-200 dark:border-slate-700'
          }`}
          onClick={() => toggleExpanded(endpoint.id)}
        >
          <div className="flex justify-between items-center gap-4 mb-2">
            <h2 className="text-lg font-semibold text-gray-900 dark:text-slate-100 m-0">{endpoint.name}</h2>
            <div className="flex items-center gap-3">
              <span className={statusClassName(endpoint.currentStatus)}>
                {statusLabel(endpoint.currentStatus)}
              </span>
              <button
                type="button"
                className="px-3 py-1.5 bg-white dark:bg-slate-800 text-red-700 dark:text-red-400 border border-red-200 dark:border-red-900/50 rounded-md text-xs font-semibold cursor-pointer transition-colors enabled:hover:bg-red-50 dark:enabled:hover:bg-red-950/30 enabled:hover:border-red-300 dark:enabled:hover:border-red-800 disabled:opacity-60 disabled:cursor-not-allowed"
                onClick={(e) => {
                  e.stopPropagation()
                  handleDelete(endpoint)
                }}
                disabled={
                  deleteMutation.isPending &&
                  deleteMutation.variables === endpoint.id
                }
                aria-label={`Delete ${endpoint.name}`}
              >
                {deleteMutation.isPending && deleteMutation.variables === endpoint.id
                  ? 'Deleting...'
                  : 'Delete'}
              </button>
            </div>
          </div>
          <a
            className="block text-sm text-blue-500 dark:text-blue-400 no-underline break-all mb-2 hover:underline"
            href={endpoint.url}
            target="_blank"
            rel="noopener noreferrer"
            onClick={(e) => e.stopPropagation()}
          >
            {endpoint.url}
          </a>
          <EndpointSparkline endpointId={endpoint.id} />
          <div className="flex justify-between text-sm text-gray-500 dark:text-slate-400">
            <span>Checks every {endpoint.checkIntervalSeconds}s</span>
            <span>Last checked: {formatLastChecked(endpoint.lastCheckedAt)}</span>
          </div>
          {expandedId === endpoint.id && (
            <EndpointDetail 
              endpointId={endpoint.id} 
              responseTimeThresholdMs={endpoint.responseTimeThresholdMs}
              failureThreshold={endpoint.failureThreshold}
            />
          )}
        </li>
      ))}
    </ul>
  )
}
