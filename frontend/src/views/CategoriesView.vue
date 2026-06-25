<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { api } from '@/api/api'
import DashboardNavbar from '@/components/DashboardNavbar.vue'

const { t } = useI18n()
const router = useRouter()

const categories = ref([])
const isLoading = ref(false)
const form = ref({ name: '', color: '#6366f1' })
const isSubmitting = ref(false)

const editingId = ref(null)
const editForm = ref({ name: '', color: '' })

onMounted(async () => {
  isLoading.value = true
  try {
    const response = await api.getCategories()
    categories.value = response.data
  } finally {
    isLoading.value = false
  }
})

async function addCategory() {
  isSubmitting.value = true
  try {
    const response = await api.addCategory(form.value)
    categories.value = response.data
    form.value = { name: '', color: '#6366f1' }
  } finally {
    isSubmitting.value = false
  }
}

function startEdit(cat) {
  editingId.value = cat.categoryId
  editForm.value = { name: cat.name, color: cat.color || '#6366f1' }
}

function cancelEdit() {
  editingId.value = null
}

async function saveEdit(cat) {
  const response = await api.updateCategory(cat.categoryId, editForm.value)
  categories.value = response.data
  editingId.value = null
}

async function deleteCategory(cat) {
  const response = await api.deleteCategory(cat.categoryId)
  categories.value = response.data
}
</script>

<template>
  <div class="app-page">

    <DashboardNavbar />

    <main class="content">

      <div class="page-header">
        <button class="btn-ghost back-btn" @click="router.push('/dashboard')">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <polyline points="15 18 9 12 15 6"/>
          </svg>
          {{ t('categories.back') }}
        </button>
        <h1 class="page-title">{{ t('categories.title') }}</h1>
      </div>

      <div class="add-form">
        <h2 class="section-title">{{ t('categories.addTitle') }}</h2>
        <form class="form-row" @submit.prevent="addCategory">
          <input
            v-model="form.name"
            class="form-input"
            type="text"
            :placeholder="t('categories.namePlaceholder')"
            required
          />
          <div class="color-field">
            <label class="color-label">{{ t('categories.color') }}</label>
            <input v-model="form.color" class="color-input" type="color" />
          </div>
          <button type="submit" class="btn-primary" :disabled="isSubmitting">
            {{ t('categories.add') }}
          </button>
        </form>
      </div>

      <div class="list-section">
        <h2 class="section-title">{{ t('categories.listTitle') }}</h2>

        <div v-if="isLoading" class="empty-state">
          <span class="spinner-dark"></span>
        </div>

        <div v-else-if="!categories.length" class="empty-state">
          <p>{{ t('categories.empty') }}</p>
        </div>

        <ul v-else class="category-list">
          <li v-for="cat in categories" :key="cat.categoryId" class="category-item">

            <template v-if="editingId === cat.categoryId">
              <input v-model="editForm.color" class="color-input" type="color" />
              <input v-model="editForm.name" class="inline-input" type="text" required @keyup.enter="saveEdit(cat)" @keyup.esc="cancelEdit" />
              <div class="item-actions">
                <button class="btn-action btn-edit" :title="t('categories.save')" @click="saveEdit(cat)">
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                    <polyline points="20 6 9 17 4 12"/>
                  </svg>
                </button>
                <button class="btn-action btn-cancel" :title="t('categories.cancel')" @click="cancelEdit">
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                    <line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/>
                  </svg>
                </button>
              </div>
            </template>

            <template v-else>
              <span class="color-dot" :style="{ background: cat.color || '#6366f1' }"></span>
              <span class="category-name">{{ cat.name }}</span>
              <div class="item-actions">
                <button class="btn-action btn-edit" :title="t('categories.edit')" @click="startEdit(cat)">
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
                    <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
                  </svg>
                </button>
                <button class="btn-action btn-deactivate" :title="t('categories.delete')" @click="deleteCategory(cat)">
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <polyline points="3 6 5 6 21 6"/>
                    <path d="M19 6l-1 14a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2L5 6"/>
                    <path d="M10 11v6"/><path d="M14 11v6"/>
                    <path d="M9 6V4a1 1 0 0 1 1-1h4a1 1 0 0 1 1 1v2"/>
                  </svg>
                </button>
              </div>
            </template>

          </li>
        </ul>
      </div>

    </main>
  </div>
</template>

<style scoped>
.content {
  max-width: 600px;
  margin: 0 auto;
  padding: 2.5rem 1.5rem;
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.page-header {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.back-btn {
  display: inline-flex;
  align-items: center;
  gap: 0.3rem;
  padding: 0.4rem 0.75rem;
}

.back-btn svg {
  width: 16px;
  height: 16px;
}

.page-title {
  font-size: 1.4rem;
  font-weight: 700;
  color: #f1f5f9;
  margin: 0;
  letter-spacing: -0.02em;
}

.section-title {
  font-size: 0.78rem;
  font-weight: 600;
  color: #64748b;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  margin: 0 0 0.75rem;
}

.add-form {
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 14px;
  padding: 1.5rem;
}

.form-row {
  display: flex;
  gap: 0.75rem;
  align-items: flex-end;
}

.form-input {
  flex: 1;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  padding: 0.55rem 0.75rem;
  color: #f1f5f9;
  font-size: 0.9rem;
  outline: none;
  transition: border-color 0.2s;
}

.form-input:focus {
  border-color: rgba(99, 102, 241, 0.5);
}

.form-input::placeholder {
  color: #475569;
}

.color-field {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.25rem;
}

.color-label {
  font-size: 0.7rem;
  color: #64748b;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.color-input {
  width: 36px;
  height: 36px;
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  padding: 2px;
  background: transparent;
  cursor: pointer;
}

.list-section {
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 14px;
  padding: 1.5rem;
}

.category-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.category-item {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.75rem 1rem;
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 10px;
}

.item-actions {
  margin-left: auto;
  display: flex;
  gap: 0.4rem;
}

.inline-input {
  flex: 1;
  background: rgba(255, 255, 255, 0.07);
  border: 1px solid rgba(99, 102, 241, 0.4);
  border-radius: 6px;
  padding: 0.3rem 0.6rem;
  color: #f1f5f9;
  font-size: 0.9rem;
  outline: none;
}

.color-dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  flex-shrink: 0;
}

.category-name {
  font-size: 0.9rem;
  color: #e2e8f0;
}

</style>
