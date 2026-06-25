<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import '@/assets/auth.css'
import { useI18n } from 'vue-i18n'
import AuthHeader from '@/components/AuthHeader.vue'
import FormField from '@/components/FormField.vue'
import { api } from '@/api/api'

const { t } = useI18n()
const authStore = useAuthStore()
const router = useRouter()

const email = ref('')
const password = ref('')
const authErrorMsg = ref('')
const isLoading = ref(false)

const isFormValid = computed(() =>
  email.value.trim() !== '' && password.value.trim() !== ''
)

async function handleLogin() {
  if (!isFormValid.value) return
  authErrorMsg.value = ''
  isLoading.value = true
  try {
    const response = await api.login({
      email: email.value,
      password: password.value
    })
    authStore.setToken(response.data.token)
    router.push('/dashboard')
  } catch (e) {
    authErrorMsg.value = t('auth.login.error')
    password.value = ''
  } finally {
    isLoading.value = false
  }
}
</script>

<template>
  <div class="auth-page">
    <div class="auth-card">
      <AuthHeader :subtitle="t('auth.login.subtitle')" />

      <form @submit.prevent="handleLogin" class="auth-form" novalidate>
        <FormField v-model="email" :label="t('auth.fields.email')" id="email" type="email" :placeholder="t('auth.fields.emailPlaceholder')" autocomplete="email" />
        <FormField v-model="password" :label="t('auth.fields.password')" id="password" type="password" :placeholder="t('auth.fields.passwordPlaceholder')" autocomplete="current-password" />

        <Transition name="error">
          <p v-if="authErrorMsg" class="error-msg">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-8-5a.75.75 0 01.75.75v4.5a.75.75 0 01-1.5 0v-4.5A.75.75 0 0110 5zm0 10a1 1 0 100-2 1 1 0 000 2z" clip-rule="evenodd"/>
            </svg>
            {{ authErrorMsg }}
          </p>
        </Transition>

        <button type="submit" class="submit-btn" :disabled="isLoading || !isFormValid">
          <span v-if="!isLoading">{{ t('auth.login.submit') }}</span>
          <span v-else class="spinner"></span>
        </button>

        <button type="button" class="auth-link-btn" @click="router.push('/register')">
          {{ t('auth.login.noAccount') }} <span>{{ t('auth.login.registerLink') }}</span>
        </button>
      </form>
    </div>
  </div>
</template>
