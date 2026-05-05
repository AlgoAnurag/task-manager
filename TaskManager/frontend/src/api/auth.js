import client from './client'

export const authAPI = {
  signup: (data) => client.post('/auth/signup', data),
  login: (data) => client.post('/auth/login', data),
  getHealth: () => client.get('/auth/health')
}
