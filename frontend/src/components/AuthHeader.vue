<script setup>
import { useI18n } from 'vue-i18n'

defineProps({
  subtitle: { type: String, required: true }
})

const { t } = useI18n()
</script>

<template>
  <div class="auth-header">
    <div class="logo-icon">
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <rect x="2" y="5" width="20" height="14" rx="2"/>
        <path d="M2 10h20"/>
      </svg>
    </div>
    <h1>{{ t('app.name') }}</h1>
    <p>{{ subtitle }}</p>
  </div>
</template>
