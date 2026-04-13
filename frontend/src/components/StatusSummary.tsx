import { useQuery } from '@tanstack/react-query'
import { fetchEndpoints } from '../api/client'
import type { EndpointDTO } from '../types/api'

export function StatusSummary() {
  const { data: endpoints } = useQuery({
    queryKey: ['endpoints'],
    queryFn: fetchEndpoints,
  })

  if (!endpoints || endpoints.length === 0) return null

  const total = endpoints.length
  const up = endpoints.filter((e: EndpointDTO) => e.currentStatus === 'UP').length
  const down = endpoints.filter((e: EndpointDTO) => e.currentStatus === 'DOWN').length
  const degraded = endpoints.filter((e: EndpointDTO) => e.currentStatus === 'DEGRADED').length
  const unknown = total - up - down - degraded

  return (
    <div className="mb-6 flex flex-wrap items-center gap-x-6 gap-y-2 text-sm">
      <span className="text-gray-600 dark:text-slate-400">
        <strong className="text-gray-900 dark:text-slate-100 text-base">{total}</strong>{' '}
        {total === 1 ? 'endpoint monitored' : 'endpoints monitored'}
      </span>
      <span className="flex items-center gap-1.5">
        <span className="w-2 h-2 rounded-full bg-green-500"></span>
        <span className="font-semibold text-gray-900 dark:text-slate-100">{up}</span>
        <span className="text-gray-500 dark:text-slate-400">UP</span>
      </span>
      <span className="flex items-center gap-1.5">
        <span className="w-2 h-2 rounded-full bg-red-500"></span>
        <span className="font-semibold text-gray-900 dark:text-slate-100">{down}</span>
        <span className="text-gray-500 dark:text-slate-400">DOWN</span>
      </span>
      {degraded > 0 && (
        <span className="flex items-center gap-1.5">
          <span className="w-2 h-2 rounded-full bg-amber-500"></span>
          <span className="font-semibold text-gray-900 dark:text-slate-100">{degraded}</span>
          <span className="text-gray-500 dark:text-slate-400">DEGRADED</span>
        </span>
      )}
      {unknown > 0 && (
        <span className="flex items-center gap-1.5">
          <span className="w-2 h-2 rounded-full bg-gray-400"></span>
          <span className="font-semibold text-gray-900 dark:text-slate-100">{unknown}</span>
          <span className="text-gray-500 dark:text-slate-400">UNKNOWN</span>
        </span>
      )}
    </div>
  )
}
