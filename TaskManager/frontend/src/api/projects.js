import client from './client'

export const projectsAPI = {
  create: (data) => client.post('/projects', data),
  getAll: () => client.get('/projects'),
  getById: (id) => client.get(`/projects/${id}`),
  update: (id, data) => client.put(`/projects/${id}`, data),
  delete: (id) => client.delete(`/projects/${id}`),
  addMember: (projectId, memberId) => client.post(`/projects/${projectId}/members/${memberId}`),
  removeMember: (projectId, memberId) => client.delete(`/projects/${projectId}/members/${memberId}`)
}
