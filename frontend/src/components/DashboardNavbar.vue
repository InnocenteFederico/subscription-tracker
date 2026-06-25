<script setup>
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { useAuthStore } from '@/stores/auth'

const { t } = useI18n()
const router = useRouter()
const authStore = useAuthStore()

function handleLogout() {
  authStore.logout()
  router.push('/login')
}
</script>

<template>
  <nav class="navbar">
    <span class="navbar-brand">{{ t('app.name') }}</span>
    <div class="navbar-actions">
      <button class="btn-secondary" @click="router.push('/categories')">{{ t('dashboard.categories') }}</button>
      <button class="btn-ghost" @click="handleLogout">{{ t('dashboard.logout') }}</button>
    </div>
  </nav>
</template>

<style scoped>
.navbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem 2rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
  background: rgba(255, 255, 255, 0.03);
  backdrop-filter: blur(12px);
  position: sticky;
  top: 0;
  z-index: 10;
}

.navbar-brand {
  font-size: 1rem;
  font-weight: 700;
  color: #f1f5f9;
  letter-spacing: -0.02em;
}

.navbar-actions {
  display: flex;
  gap: 0.75rem;
  align-items: center;
}

</style>
