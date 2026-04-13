import { useQuery } from '@tanstack/react-query'
import { LineChart, Line, ResponsiveContainer } from 'recharts'
import { fetchResults } from '../api/client'
import type { HealthCheckResultDTO, HealthStatus } from '../types/api'

interface EndpointSparklineProps {
  endpointId: number
}

const STATUS_COLORS: Record<HealthStatus | 'UNKNOWN', string> = {
  UP: '#10b981',
  DOWN: '#ef4444',
  DEGRADED: '#f59e0b',
  UNKNOWN: '#9ca3af',
}

export function EndpointSparkline({ endpointId }: EndpointSparklineProps) {
  const { data: results } = useQuery({
    queryKey: ['results', endpointId],
    queryFn: () => fetchResults(endpointId, 20),
  })

  if (!results || results.length === 0) {
    return (
      <div className="h-8 flex items-center text-xs text-gray-400 italic">
        No data yet
      </div>
    )
  }

  const lastStatus = results[0]?.status ?? 'UNKNOWN'
  const lineColor = STATUS_COLORS[lastStatus]

  const chartData = [...results].reverse().map((r: HealthCheckResultDTO) => ({
    responseTimeMs: r.responseTimeMs ?? 0,
  }))

  return (
    <div className="h-8 w-full my-2">
      <ResponsiveContainer width="100%" height="100%">
        <LineChart data={chartData} margin={{ top: 2, right: 2, bottom: 2, left: 2 }}>
          <Line
            type="monotone"
            dataKey="responseTimeMs"
            stroke={lineColor}
            strokeWidth={1.75}
            dot={false}
            isAnimationActive={false}
          />
        </LineChart>
      </ResponsiveContainer>
    </div>
  )
}
