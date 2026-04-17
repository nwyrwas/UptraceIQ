import { useQuery } from '@tanstack/react-query'
import {
  CartesianGrid,
  Line,
  LineChart,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from 'recharts'
import { fetchResults, fetchUptime } from '../api/client'
import type { HealthCheckResultDTO } from '../types/api'

interface EndpointDetailProps {
  endpointId: number
  responseTimeThresholdMs: number
  failureThreshold: number
}

interface ChartPoint {
  time: string
  responseTimeMs: number | null
  status: string
}

function formatUptime(percentage: number | null): string {
  if (percentage === null) return '—'
  return `${percentage.toFixed(2)}%`
}

function formatTime(timestamp: string): string {
  return new Date(timestamp).toLocaleTimeString([], {
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
  })
}

function toChartPoints(results: HealthCheckResultDTO[]): ChartPoint[] {
  return [...results].reverse().map((r) => ({
    time: formatTime(r.checkedAt),
    responseTimeMs: r.responseTimeMs,
    status: r.status,
  }))
}

export function EndpointDetail({ endpointId, responseTimeThresholdMs, failureThreshold }: EndpointDetailProps) {
  const uptimeQuery = useQuery({
    queryKey: ['uptime', endpointId],
    queryFn: () => fetchUptime(endpointId, 100),
  })

  const resultsQuery = useQuery({
    queryKey: ['results', endpointId],
    queryFn: () => fetchResults(endpointId, 20),
  })

  if (uptimeQuery.isLoading || resultsQuery.isLoading) {
    return <div className="text-center text-gray-500 dark:text-slate-400 p-4 text-sm">Loading details...</div>
  }

  if (uptimeQuery.error || resultsQuery.error) {
    return (
      <div className="text-center text-red-600 dark:text-red-400 p-4 text-sm">
        Failed to load details.
      </div>
    )
  }

  const uptime = uptimeQuery.data
  const results = resultsQuery.data ?? []
  const chartData = toChartPoints(results)

  return (
    <div className="mt-4 pt-4 border-t border-gray-200 dark:border-slate-700 flex flex-col gap-4">
      <div className="grid grid-cols-3 gap-3">
        <Stat
          label="Uptime (last 100)"
          value={formatUptime(uptime?.uptimePercentage ?? null)}
        />
        <Stat
          label="Total checks"
          value={uptime?.totalChecks?.toString() ?? '0'}
        />
        <Stat
          label="Successful"
          value={uptime?.upChecks?.toString() ?? '0'}
        />
        <Stat 
          label="Degraded above" value={`${responseTimeThresholdMs}ms`} 
        />
        <Stat 
          label="Alert after" value={`${failureThreshold} failure${failureThreshold === 1 ? '' : 's'}`} 
          />
      </div>

      {chartData.length === 0 ? (
        <p className="text-center text-gray-500 dark:text-slate-400 p-4 text-sm">No check history yet.</p>
      ) : (
        <div className="pt-2">
          <h3 className="text-sm font-semibold text-gray-700 dark:text-slate-300 m-0 mb-2">
            Response time (last {chartData.length} checks)
          </h3>
          <ResponsiveContainer width="100%" height={200}>
            <LineChart data={chartData}>
              <CartesianGrid strokeDasharray="3 3" stroke="#e5e7eb" />
              <XAxis dataKey="time" tick={{ fontSize: 11 }} stroke="#6b7280" />
              <YAxis
                tick={{ fontSize: 11 }}
                stroke="#6b7280"
                label={{
                  value: 'ms',
                  angle: -90,
                  position: 'insideLeft',
                  style: { fontSize: 11, fill: '#6b7280' },
                }}
              />
              <Tooltip />
              <Line
                type="monotone"
                dataKey="responseTimeMs"
                stroke="#3b82f6"
                strokeWidth={2}
                dot={{ r: 3 }}
                activeDot={{ r: 5 }}
              />
            </LineChart>
          </ResponsiveContainer>
        </div>
      )}
    </div>
  )
}

interface StatProps {
  label: string
  value: string
}

function Stat({ label, value }: StatProps) {
  return (
    <div className="bg-gray-50 dark:bg-slate-900/50 border border-gray-200 dark:border-slate-700 rounded-md px-4 py-3">
      <div className="text-xs font-medium text-gray-500 dark:text-slate-400 uppercase tracking-wider mb-1">{label}</div>
      <div className="text-xl font-semibold text-gray-900 dark:text-slate-100">{value}</div>
    </div>
  )
}
