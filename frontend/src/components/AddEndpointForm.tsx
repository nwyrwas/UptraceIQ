import { useState, type FormEvent } from 'react'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { createEndpoint, ApiError } from '../api/client'
import type { CreateEndpointRequest } from '../types/api'


export function AddEndpointForm() {
  const [name, setName] = useState('')
  const [url, setUrl] = useState('')
  const [checkIntervalSeconds, setCheckIntervalSeconds] = useState(30)

  const queryClient = useQueryClient()

  const mutation = useMutation({
    mutationFn: (body: CreateEndpointRequest) => createEndpoint(body),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['endpoints'] })
      setName('')
      setUrl('')
      setCheckIntervalSeconds(30)
    },
  })

  function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    mutation.mutate({
      name: name.trim(),
      url: url.trim(),
      checkIntervalSeconds,
      enabled: true,
    })
  }

  const errorMessage =
    mutation.error instanceof ApiError
      ? `API error (${mutation.error.status}): ${mutation.error.statusText}`
      : mutation.error?.message

  return (
    <form className="bg-white dark:bg-slate-800 border border-gray-200 dark:border-slate-700 rounded-lg p-6 flex flex-col gap-4 shadow-sm lg:sticky lg:top-6" onSubmit={handleSubmit}>
      <h2 className="text-lg font-semibold text-gray-900 dark:text-slate-100 m-0">Add Endpoint</h2>

      <div className="flex flex-col gap-1.5">
        <label htmlFor="endpoint-name" className="text-sm font-medium text-gray-700 dark:text-slate-300">Name</label>
        <input
          id="endpoint-name"
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="e.g. GitHub"
          required
          className="px-3 py-2 border border-gray-300 dark:border-slate-600 rounded-md text-base text-gray-900 dark:text-slate-100 bg-white dark:bg-slate-900 placeholder:text-gray-400 dark:placeholder:text-slate-500 transition-colors focus:outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20"
        />
      </div>

      <div className="flex flex-col gap-1.5">
        <label htmlFor="endpoint-url" className="text-sm font-medium text-gray-700 dark:text-slate-300">URL</label>
        <input
          id="endpoint-url"
          type="url"
          value={url}
          onChange={(e) => setUrl(e.target.value)}
          placeholder="https://example.com"
          required
          className="px-3 py-2 border border-gray-300 dark:border-slate-600 rounded-md text-base text-gray-900 dark:text-slate-100 bg-white dark:bg-slate-900 placeholder:text-gray-400 dark:placeholder:text-slate-500 transition-colors focus:outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20"
        />
      </div>

      <div className="flex flex-col gap-1.5">
        <label htmlFor="endpoint-interval" className="text-sm font-medium text-gray-700 dark:text-slate-300">Check interval (seconds)</label>
        <input
          id="endpoint-interval"
          type="number"
          min={5}
          max={3600}
          value={checkIntervalSeconds}
          onChange={(e) => setCheckIntervalSeconds(Number(e.target.value))}
          required
          className="px-3 py-2 border border-gray-300 dark:border-slate-600 rounded-md text-base text-gray-900 dark:text-slate-100 bg-white dark:bg-slate-900 placeholder:text-gray-400 dark:placeholder:text-slate-500 transition-colors focus:outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20"
        />
      </div>

      {errorMessage && (
        <p className="m-0 px-3 py-2.5 bg-red-100 dark:bg-red-950/50 border border-red-200 dark:border-red-900 rounded-md text-red-800 dark:text-red-300 text-sm">
          {errorMessage}
        </p>
      )}

      <button
        type="submit"
        className="px-4 py-2.5 bg-blue-500 text-white rounded-md text-base font-semibold cursor-pointer transition-colors hover:bg-blue-600 disabled:bg-blue-300 dark:disabled:bg-blue-800 disabled:cursor-not-allowed"
        disabled={mutation.isPending}
      >
        {mutation.isPending ? 'Adding...' : 'Add Endpoint'}
      </button>
    </form>
  )
}
