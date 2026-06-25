import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import { setActivePinia, createPinia } from 'pinia'
import { createRouter, createMemoryHistory } from 'vue-router'
import { createI18n } from 'vue-i18n'
import DashboardNavbar from '../DashboardNavbar.vue'
import { useAuthStore } from '@/stores/auth'
import messages from '@/locales/it.json'

const i18n = createI18n({ legacy: false, locale: 'it', messages: { it: messages } })

function buildRouter() {
  return createRouter({
    history: createMemoryHistory(),
    routes: [
      { path: '/login', component: { template: '<div/>' } },
      { path: '/dashboard', component: { template: '<div/>' } },
      { path: '/categories', component: { template: '<div/>' } },
    ],
  })
}

describe('DashboardNavbar', () => {
  let pinia, router

  beforeEach(() => {
    pinia = createPinia()
    setActivePinia(pinia)
    router = buildRouter()
  })

  function mountNavbar() {
    return mount(DashboardNavbar, {
      global: { plugins: [pinia, router, i18n] },
    })
  }

  it('mostra il nome dell\'applicazione', () => {
    const wrapper = mountNavbar()
    expect(wrapper.find('.navbar-brand').text()).toBe('Subscription Tracker')
  })

  it('il pulsante Categorie naviga a /categories', async () => {
    const wrapper = mountNavbar()
    await wrapper.find('.btn-secondary').trigger('click')
    await router.isReady()
    expect(router.currentRoute.value.path).toBe('/categories')
  })

  it('il pulsante Logout chiama authStore.logout e naviga a /login', async () => {
    const wrapper = mountNavbar()
    const authStore = useAuthStore()
    authStore.setToken('test-token')
    const logoutSpy = vi.spyOn(authStore, 'logout')

    await wrapper.find('.btn-ghost').trigger('click')
    await router.isReady()

    expect(logoutSpy).toHaveBeenCalledOnce()
    expect(router.currentRoute.value.path).toBe('/login')
  })

  it('logout rimuove il token dallo store', async () => {
    const wrapper = mountNavbar()
    const authStore = useAuthStore()
    authStore.setToken('test-token')

    await wrapper.find('.btn-ghost').trigger('click')

    expect(authStore.isAuthenticated).toBe(false)
  })
})
