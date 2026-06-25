<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { api } from '@/api/api'

const { t } = useI18n()
const router = useRouter()

defineProps({
  show: { type: Boolean, required: true },
  categories: { type: Array, default: () => [] }
})

const emit = defineEmits(['close', 'submitted'])

function emptyForm() {
  return { id: null, name: '', amount: '', currency: 'EUR', billingCycle: 'MONTHLY', firstBilling: '', categoryId: null, notes: '' }
}

const form = ref(emptyForm())
const isEditMode = computed(() => !!form.value.id)

function open(subscription = null) {
  form.value = subscription
    ? { ...subscription }
    : emptyForm()
}

async function submit() {
  const response = isEditMode.value
    ? await api.updateSubscription(form.value)
    : await api.addSubscription(form.value)
  emit('submitted', response.data)
}

defineExpose({ open })
</script>

<template>
  <Teleport to="body">
    <div v-if="show" class="modal-backdrop" @click.self="emit('close')">
      <div class="modal">
        <h3 class="modal-title">
          {{ isEditMode ? t('dashboard.subscriptions.modal.titleEdit') : t('dashboard.subscriptions.modal.titleAdd') }}
        </h3>

        <form class="modal-form" @submit.prevent="submit">

          <div class="form-row">
            <label class="form-label">{{ t('dashboard.subscriptions.modal.fieldName') }}</label>
            <input v-model="form.name" class="form-input" type="text" required />
          </div>

          <div class="form-row">
            <label class="form-label">{{ t('dashboard.subscriptions.modal.fieldAmount') }}</label>
            <input v-model="form.amount" class="form-input" type="number" min="0" step="0.01" required />
          </div>

          <div class="form-row form-row--split">
            <div>
              <label class="form-label">{{ t('dashboard.subscriptions.modal.fieldCycle') }}</label>
              <select v-model="form.billingCycle" class="form-input">
                <option value="WEEKLY">{{ t('billingCycle.WEEKLY') }}</option>
                <option value="MONTHLY">{{ t('billingCycle.MONTHLY') }}</option>
                <option value="YEARLY">{{ t('billingCycle.YEARLY') }}</option>
              </select>
            </div>
            <div>
              <label class="form-label">{{ t('dashboard.subscriptions.modal.fieldFirstBilling') }}</label>
              <input v-model="form.firstBilling" class="form-input" type="date" required />
            </div>
          </div>

          <div class="form-row">
            <div class="label-row">
              <label class="form-label">{{ t('dashboard.subscriptions.modal.fieldCategory') }}</label>
              <button type="button" class="link-btn" @click="router.push('/categories')">
                + {{ t('categories.addTitle') }}
              </button>
            </div>
            <select v-model="form.categoryId" class="form-input">
              <option :value="null">{{ t('dashboard.subscriptions.modal.noCategory') }}</option>
              <option v-for="cat in categories" :key="cat.categoryId" :value="cat.categoryId">{{ cat.name }}</option>
            </select>
          </div>

          <div class="form-row">
            <label class="form-label">{{ t('dashboard.subscriptions.modal.fieldNotes') }}</label>
            <textarea v-model="form.notes" class="form-input form-textarea" :placeholder="t('dashboard.subscriptions.modal.placeholderNotes')"></textarea>
          </div>

          <div class="modal-actions">
            <button type="button" class="btn-ghost" @click="emit('close')">{{ t('dashboard.subscriptions.modal.cancel') }}</button>
            <button type="submit" class="btn-primary">{{ t('dashboard.subscriptions.modal.confirm') }}</button>
          </div>

        </form>
      </div>
    </div>
  </Teleport>
</template>

<style scoped>
.modal-backdrop {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.6);
  backdrop-filter: blur(4px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 100;
}

.modal {
  background: #1e1b4b;
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  padding: 2rem;
  width: 100%;
  max-width: 480px;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: #f1f5f9;
  margin: 0 0 1.5rem;
}

.modal-form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.form-row {
  display: flex;
  flex-direction: column;
  gap: 0.35rem;
}

.form-row--split {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 0.75rem;
}

.form-row--split > div {
  display: flex;
  flex-direction: column;
  gap: 0.35rem;
}

.form-label {
  font-size: 0.78rem;
  font-weight: 500;
  color: #64748b;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.form-input {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  padding: 0.55rem 0.75rem;
  color: #f1f5f9;
  font-size: 0.9rem;
  outline: none;
  transition: border-color 0.2s;
  width: 100%;
  box-sizing: border-box;
}

.form-input:focus {
  border-color: rgba(99, 102, 241, 0.5);
}

.form-input option {
  background: #1e1b4b;
}

.form-input[type='number']::-webkit-inner-spin-button,
.form-input[type='number']::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

.form-input[type='number'] {
  -moz-appearance: textfield;
}

.label-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.link-btn {
  background: none;
  border: none;
  padding: 0;
  font-size: 0.75rem;
  color: #818cf8;
  cursor: pointer;
  transition: color 0.2s;
}

.link-btn:hover {
  color: #a5b4fc;
}

.form-textarea {
  resize: vertical;
  min-height: 80px;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
  margin-top: 0.5rem;
}
</style>
