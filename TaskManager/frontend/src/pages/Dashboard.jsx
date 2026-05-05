import React, { useState, useEffect } from 'react'
import { dashboardAPI } from '../api/dashboard'

const Dashboard = () => {
  const [dashboard, setDashboard] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    const fetchDashboard = async () => {
      try {
        const response = await dashboardAPI.getDashboard()
        setDashboard(response.data)
      } catch (err) {
        setError('Failed to load dashboard')
      } finally {
        setLoading(false)
      }
    }

    fetchDashboard()
  }, [])

  if (loading) return <div className="text-center py-8">Loading...</div>
  if (error) return <div className="text-red-600 text-center py-8">{error}</div>
  if (!dashboard) return <div className="text-center py-8">No data available</div>

  return (
    <div className="space-y-8">
      <h1 className="text-3xl font-bold">Dashboard</h1>

      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <StatCard label="Total Projects" value={dashboard.totalProjects} />
        <StatCard label="Total Tasks" value={dashboard.totalTasks} />
        <StatCard label="Completed Tasks" value={dashboard.completedTasks} />
        <StatCard label="Overdue Tasks" value={dashboard.overdueTasks} className="bg-red-50" />
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
        <div className="bg-white p-6 rounded-lg shadow">
          <h2 className="text-xl font-bold mb-4">Recent Tasks</h2>
          <div className="space-y-2">
            {dashboard.recentTasks?.length > 0 ? (
              dashboard.recentTasks.map(task => (
                <div key={task.id} className="p-3 border rounded hover:bg-gray-50">
                  <div className="font-semibold text-sm">{task.title}</div>
                  <div className="text-xs text-gray-500 mt-1">
                    <span className={`inline-block px-2 py-1 rounded text-xs mr-2 ${
                      task.status === 'COMPLETED' ? 'bg-green-100 text-green-800' :
                      task.status === 'IN_PROGRESS' ? 'bg-blue-100 text-blue-800' :
                      'bg-gray-100 text-gray-800'
                    }`}>
                      {task.status}
                    </span>
                  </div>
                </div>
              ))
            ) : (
              <p className="text-gray-500">No recent tasks</p>
            )}
          </div>
        </div>

        <div className="bg-white p-6 rounded-lg shadow">
          <h2 className="text-xl font-bold mb-4">Recent Projects</h2>
          <div className="space-y-2">
            {dashboard.recentProjects?.length > 0 ? (
              dashboard.recentProjects.map(project => (
                <div key={project.id} className="p-3 border rounded hover:bg-gray-50">
                  <div className="font-semibold text-sm">{project.name}</div>
                  <div className="text-xs text-gray-500 mt-1">{project.members?.length || 0} members</div>
                </div>
              ))
            ) : (
              <p className="text-gray-500">No recent projects</p>
            )}
          </div>
        </div>
      </div>
    </div>
  )
}

const StatCard = ({ label, value, className = '' }) => (
  <div className={`${className} bg-white p-6 rounded-lg shadow`}>
    <div className="text-gray-600 text-sm">{label}</div>
    <div className="text-3xl font-bold mt-2">{value}</div>
  </div>
)

export default Dashboard
