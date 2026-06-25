import client from './client'

export const api = {

  // ---- AUTH ----

  login: (credentials) => client.post('/auth/login', credentials),
  register: (credentials) => client.post('/auth/register', credentials),

  // ---- SUBSCRIPTIOSN ----

  getSubscriptions: (params) => client.get('/subscriptions', params),
  addSubscription: (data) => client.post('/subscriptions', data),
  updateSubscription: (sub) => client.patch('/subscriptions/' + sub.id, sub),

  // ---- CATEGORIES ----

  getCategories: () => client.get('/categories'),
  addCategory: (data) => client.post('/categories', data),
  updateCategory: (id, data) => client.patch(`/categories/${id}`, data),
  deleteCategory: (id) => client.delete(`/categories/${id}`),

  // ---- STATS ----

  getSummary: () => client.get('/stats/summary')

}
