import client from './client'

export const tasksAPI = {
  create: (data) => client.post('/tasks', data),
  getById: (id) => client.get(`/tasks/${id}`),
  getByProject: (projectId) => client.get(`/tasks/project/${projectId}`),
  getAssigned: () => client.get('/tasks/assigned/me'),
  getOverdue: () => client.get('/tasks/overdue/me'),
  update: (id, data) => client.put(`/tasks/${id}`, data),
  delete: (id) => client.delete(`/tasks/${id}`)
}
