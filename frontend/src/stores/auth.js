import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', () => {

  // Allineo ref a localStorage per gestire refresh
  const token = ref(localStorage.getItem('token'))

  // Computed per verificare l'autenticazione
  const isAuthenticated = computed(() => !!token.value)

  // Valorizzazione del token e allineamento localStorage con ref
  function setToken(newToken) {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  // Pulizia di token e localStorage al logout
  function logout() {
    token.value = null
    localStorage.removeItem('token')
  }

  return { token, isAuthenticated, setToken, logout }

})