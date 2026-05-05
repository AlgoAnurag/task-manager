import React, { useState, useEffect } from 'react'
import { useParams } from 'react-router-dom'
import { projectsAPI } from '../api/projects'
import { tasksAPI } from '../api/tasks'

const ProjectDetail = () => {
  const { id } = useParams()
  const [project, setProject] = useState(null)
  const [tasks, setTasks] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [showTaskForm, setShowTaskForm] = useState(false)
  const [taskForm, setTaskForm] = useState({ title: '', description: '', priority: 'MEDIUM' })

  useEffect(() => {
    fetchProject()
  }, [id])

  const fetchProject = async () => {
    try {
      const projectResponse = await projectsAPI.getById(id)
      setProject(projectResponse.data)

      const tasksResponse = await tasksAPI.getByProject(id)
      setTasks(tasksResponse.data)
    } catch (err) {
      setError('Failed to load project')
    } finally {
      setLoading(false)
    }
  }

  const handleCreateTask = async (e) => {
    e.preventDefault()
    try {
      await tasksAPI.create({
        ...taskForm,
        projectId: parseInt(id)
      })
      setTaskForm({ title: '', description: '', priority: 'MEDIUM' })
      setShowTaskForm(false)
      fetchProject()
    } catch (err) {
      setError('Failed to create task')
    }
  }

  const handleTaskStatusChange = async (taskId, newStatus) => {
    try {
      const task = tasks.find(t => t.id === taskId)
      await tasksAPI.update(taskId, { ...task, status: newStatus })
      fetchProject()
    } catch (err) {
      setError('Failed to update task')
    }
  }

  const handleDeleteTask = async (taskId) => {
    if (window.confirm('Delete this task?')) {
      try {
        await tasksAPI.delete(taskId)
        fetchProject()
      } catch (err) {
        setError('Failed to delete task')
      }
    }
  }

  if (loading) return <div className="text-center py-8">Loading...</div>
  if (error) return <div className="text-red-600 text-center py-8">{error}</div>
  if (!project) return <div className="text-center py-8">Project not found</div>

  return (
    <div className="space-y-8">
      <div>
        <h1 className="text-3xl font-bold">{project.name}</h1>
        <p className="text-gray-600 mt-2">{project.description}</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div className="bg-white p-4 rounded-lg shadow">
          <h3 className="font-semibold">Members ({project.members?.length || 0})</h3>
          <div className="mt-2 space-y-1">
            {project.members?.map(member => (
              <div key={member.id} className="text-sm text-gray-600">
                {member.firstName} {member.lastName}
              </div>
            ))}
          </div>
        </div>
        <div className="bg-white p-4 rounded-lg shadow">
          <h3 className="font-semibold">Tasks ({tasks.length})</h3>
          <div className="mt-2 space-y-1 text-sm text-gray-600">
            <div>Completed: {tasks.filter(t => t.status === 'COMPLETED').length}</div>
            <div>In Progress: {tasks.filter(t => t.status === 'IN_PROGRESS').length}</div>
            <div>To Do: {tasks.filter(t => t.status === 'TODO').length}</div>
          </div>
        </div>
      </div>

      <div>
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-2xl font-bold">Tasks</h2>
          <button
            onClick={() => setShowTaskForm(!showTaskForm)}
            className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
          >
            + Add Task
          </button>
        </div>

        {showTaskForm && (
          <div className="bg-white p-6 rounded-lg shadow mb-4">
            <form onSubmit={handleCreateTask} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Title</label>
                <input
                  type="text"
                  value={taskForm.title}
                  onChange={(e) => setTaskForm({ ...taskForm, title: e.target.value })}
                  required
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Description</label>
                <textarea
                  value={taskForm.description}
                  onChange={(e) => setTaskForm({ ...taskForm, description: e.target.value })}
                  rows="3"
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Priority</label>
                <select
                  value={taskForm.priority}
                  onChange={(e) => setTaskForm({ ...taskForm, priority: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg"
                >
                  <option>LOW</option>
                  <option>MEDIUM</option>
                  <option>HIGH</option>
                  <option>URGENT</option>
                </select>
              </div>
              <button type="submit" className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
                Create Task
              </button>
            </form>
          </div>
        )}

        <div className="space-y-2">
          {tasks.map(task => (
            <div key={task.id} className="bg-white p-4 rounded-lg shadow">
              <div className="flex justify-between items-start">
                <div className="flex-1">
                  <h3 className="font-semibold">{task.title}</h3>
                  <p className="text-sm text-gray-600 mt-1">{task.description}</p>
                  <div className="flex gap-2 mt-2">
                    <span className={`text-xs px-2 py-1 rounded ${
                      task.priority === 'URGENT' ? 'bg-red-100 text-red-800' :
                      task.priority === 'HIGH' ? 'bg-orange-100 text-orange-800' :
                      task.priority === 'MEDIUM' ? 'bg-yellow-100 text-yellow-800' :
                      'bg-green-100 text-green-800'
                    }`}>
                      {task.priority}
                    </span>
                  </div>
                </div>
                <select
                  value={task.status}
                  onChange={(e) => handleTaskStatusChange(task.id, e.target.value)}
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
              </div>
              <button
                onClick={() => handleDeleteTask(task.id)}
                className="mt-2 text-red-600 hover:text-red-800 text-sm"
              >
                Delete
              </button>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}

export default ProjectDetail
