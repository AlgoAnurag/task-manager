import client from './client'

export const dashboardAPI = {
  getDashboard: () => client.get('/dashboard')
}
