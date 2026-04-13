import { EndpointList } from './components/EndpointList'
import { AddEndpointForm } from './components/AddEndpointForm'
import { StatusSummary } from './components/StatusSummary'
import { ThemeToggle } from './components/ThemeToggle'

function App() {
  return (
    <div className="max-w-7xl mx-auto px-6 py-10">
      <header className="mb-8 pb-6 border-b border-gray-200 dark:border-slate-700 flex items-start justify-between gap-4">
        <div>
          <div className="flex items-center gap-3">
            <div className="w-9 h-9 rounded-lg bg-gradient-to-br from-indigo-500 to-blue-600 flex items-center justify-center shadow-sm">
              <span className="text-white font-bold text-lg">U</span>
            </div>
            <h1 className="text-3xl font-bold m-0 bg-gradient-to-r from-indigo-600 to-blue-400 bg-clip-text text-transparent">
              UptraceIQ
            </h1>
          </div>
          <p className="text-gray-500 dark:text-slate-400 mt-2 ml-12">System health and uptime monitoring</p>
        </div>
        <ThemeToggle />
      </header>

      <StatusSummary />

      <main className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-1">
          <AddEndpointForm />
        </div>
        <div className="lg:col-span-2">
          <EndpointList />
        </div>
      </main>
    </div>
  )
}

export default App
