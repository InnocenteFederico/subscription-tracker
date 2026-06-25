import { describe, it, expect, beforeEach } from "vitest";
import { setActivePinia, createPinia } from "pinia";
import { useAuthStore } from "../auth";

describe('auth store', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    localStorage.clear()
  })

  it('setToken salva il token nello state e in localStorage', () => {
    const auth = useAuthStore()

    auth.setToken('token')

    expect(auth.token).toBe('token')
    expect(localStorage.getItem('token')).toBe('token')
  })

  it('isAuthernticated è calcolato correttamente', () => {
    const auth = useAuthStore()

    expect(auth.isAuthenticated).toBe(false)
    auth.setToken('token1')
    expect(auth.isAuthenticated).toBe(true)
  })

  it('logout pulisce i token da state e localStorage', () => {
    const auth = useAuthStore()
    auth.setToken('token1')

    auth.logout()

    expect(auth.token).toBeNull()
    expect(localStorage.getItem('token')).toBeNull()
  })

})