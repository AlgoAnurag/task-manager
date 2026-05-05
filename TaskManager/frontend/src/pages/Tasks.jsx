import React, { useState, useEffect } from 'react'
import { tasksAPI } from '../api/tasks'

const Tasks = () => {
  const [tasks, setTasks] = useState([])
  const [overdueTasks, setOverdueTasks] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [activeTab, setActiveTab] = useState('assigned')

  useEffect(() => {
    fetchTasks()
  }, [])

  const fetchTasks = async () => {
    try {
      const assignedResponse = await tasksAPI.getAssigned()
      setTasks(assignedResponse.data)

      const overdueResponse = await tasksAPI.getOverdue()
      setOverdueTasks(overdueResponse.data)
    } catch (err) {
      setError('Failed to load tasks')
    } finally {
      setLoading(false)
    }
  }

  const handleStatusChange = async (taskId, newStatus) => {
    try {
      const task = tasks.find(t => t.id === taskId)
      await tasksAPI.update(taskId, { ...task, status: newStatus })
      fetchTasks()
    } catch (err) {
      setError('Failed to update task')
    }
  }

  const handleDelete = async (taskId) => {
    if (window.confirm('Delete this task?')) {
      try {
        await tasksAPI.delete(taskId)
        fetchTasks()
      } catch (err) {
        setError('Failed to delete task')
      }
    }
  }

  if (loading) return <div className="text-center py-8">Loading...</div>

  return (
    <div className="space-y-8">
      <h1 className="text-3xl font-bold">My Tasks</h1>

      {error && <div className="bg-red-50 border border-red-200 text-red-600 px-4 py-3 rounded">{error}</div>}

      <div className="flex gap-4 border-b">
        <button
          onClick={() => setActiveTab('assigned')}
          className={`px-4 py-2 border-b-2 ${activeTab === 'assigned' ? 'border-blue-600 text-blue-600' : 'border-transparent'}`}
        >
          Assigned ({tasks.length})
        </button>
        <button
          onClick={() => setActiveTab('overdue')}
          className={`px-4 py-2 border-b-2 ${activeTab === 'overdue' ? 'border-red-600 text-red-600' : 'border-transparent'}`}
        >
          Overdue ({overdueTasks.length})
        </button>
      </div>

      <div className="space-y-4">
        {activeTab === 'assigned' && tasks.map(task => (
          <TaskCard
            key={task.id}
            task={task}
            onStatusChange={(status) => handleStatusChange(task.id, status)}
            onDelete={() => handleDelete(task.id)}
          />
        ))}

        {activeTab === 'overdue' && overdueTasks.map(task => (
          <TaskCard
            key={task.id}
            task={task}
            onStatusChange={(status) => handleStatusChange(task.id, status)}
            onDelete={() => handleDelete(task.id)}
            isOverdue
          />
        ))}

        {activeTab === 'assigned' && tasks.length === 0 && (
          <div className="text-center py-8 text-gray-500">No assigned tasks</div>
        )}

        {activeTab === 'overdue' && overdueTasks.length === 0 && (
          <div className="text-center py-8 text-gray-500">No overdue tasks</div>
        )}
      </div>
    </div>
  )
}

const TaskCard = ({ task, onStatusChange, onDelete, isOverdue }) => (
  <div className={`bg-white p-4 rounded-lg shadow ${isOverdue ? 'border-l-4 border-red-500' : ''}`}>
    <div className="flex justify-between items-start">
      <div className="flex-1">
        <h3 className="font-semibold">{task.title}</h3>
        <p className="text-sm text-gray-600 mt-1">{task.description}</p>
        <div className="flex gap-2 mt-3">
          <span className={`text-xs px-2 py-1 rounded ${
            task.priority === 'URGENT' ? 'bg-red-100 text-red-800' :
            task.priority === 'HIGH' ? 'bg-orange-100 text-orange-800' :
            task.priority === 'MEDIUM' ? 'bg-yellow-100 text-yellow-800' :
            'bg-green-100 text-green-800'
          }`}>
            {task.priority}
          </span>
          {isOverdue && <span className="text-xs px-2 py-1 rounded bg-red-100 text-red-800">Overdue</span>}
        </div>
      </div>
      <div className="flex gap-2">
        <select
          value={task.status}
          onChange={(e) => onStatusChange(e.target.value)}
          className={`px-2 py-1 rounded text-sm border ${
            task.status === 'COMPLETED' ? 'bg-green-100 text-green-800' :
            task.status === 'IN_PROGRESS' ? 'bg-blue-100 text-blue-800' :
            'bg-gray-100 text-gray-800'
          }`}
        >
          <option>TODO</option>
          <option>IN_PROGRESS</option>
          <option>IN_REVIEW</option>
          <option>COMPLETED</option>
          <option>CANCELLED</option>
        </select>
        <button
          onClick={onDelete}
          className="text-red-600 hover:text-red-800 text-sm px-2"
        >
          Delete
        </button>
      </div>
    </div>
  </div>
)

export default Tasks
