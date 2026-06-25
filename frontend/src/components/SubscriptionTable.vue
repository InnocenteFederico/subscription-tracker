<script setup>
import { ref, computed } from 'vue'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()

const props = defineProps({
  subscriptions: { type: Array, required: true },
  isLoading: { type: Boolean, default: false }
})

const emit = defineEmits(['add', 'deactivate', 'edit'])

const sortKey = ref(null)
const sortDir = ref('asc')
const billingCycleOrder = { WEEKLY: 0, MONTHLY: 1, YEARLY: 2 }

const sortedSubscriptions = computed(() => {
  if (!sortKey.value) return props.subscriptions
  return [...props.subscriptions].sort((a, b) => {
    let av = a[sortKey.value]
    let bv = b[sortKey.value]
    if (sortKey.value === 'billingCycle') {
      av = billingCycleOrder[av] ?? 99
      bv = billingCycleOrder[bv] ?? 99
    } else if (sortKey.value === 'nextBilling') {
      av = av ? new Date(av).getTime() : Infinity
      bv = bv ? new Date(bv).getTime() : Infinity
    } else if (sortKey.value === 'amount') {
      av = Number(av)
      bv = Number(bv)
    } else {
      av = (av ?? '').toString().toLowerCase()
      bv = (bv ?? '').toString().toLowerCase()
    }
    if (av < bv) return sortDir.value === 'asc' ? -1 : 1
    if (av > bv) return sortDir.value === 'asc' ? 1 : -1
    return 0
  })
})

function setSort(key) {
  if (sortKey.value === key) {
    sortDir.value = sortDir.value === 'asc' ? 'desc' : 'asc'
  } else {
    sortKey.value = key
    sortDir.value = 'asc'
  }
}

function sortIcon(key) {
  if (sortKey.value !== key) return '↕'
  return sortDir.value === 'asc' ? '↑' : '↓'
}

function formatCost(amount) {
  return new Intl.NumberFormat('it-IT', { style: 'currency', currency: 'EUR' }).format(amount)
}

function formatDate(date) {
  if (!date) return '—'
  return new Intl.DateTimeFormat('it-IT').format(new Date(date))
}
</script>

<template>
  <section class="section">
    <div class="section-header">
      <h2 class="section-title">{{ t('dashboard.subscriptions.title') }}</h2>
      <button class="btn-secondary btn-add" @click="emit('add')">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
          <line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/>
        </svg>
        {{ t('dashboard.subscriptions.actionAdd') }}
      </button>
    </div>

    <div class="table-wrapper">
      <table v-if="!isLoading && subscriptions.length" class="table">
        <thead>
          <tr>
            <th class="th-sortable" @click="setSort('name')">
              {{ t('dashboard.subscriptions.colName') }}
              <span class="sort-icon">{{ sortIcon('name') }}</span>
            </th>
            <th class="th-sortable" @click="setSort('amount')">
              {{ t('dashboard.subscriptions.colCost') }}
              <span class="sort-icon">{{ sortIcon('amount') }}</span>
            </th>
            <th class="th-sortable" @click="setSort('categoryName')">
              {{ t('dashboard.subscriptions.colCategory') }}
              <span class="sort-icon">{{ sortIcon('categoryName') }}</span>
            </th>
            <th class="th-sortable" @click="setSort('billingCycle')">
              {{ t('dashboard.subscriptions.colFrequency') }}
              <span class="sort-icon">{{ sortIcon('billingCycle') }}</span>
            </th>
            <th class="th-sortable" @click="setSort('nextBilling')">
              {{ t('dashboard.subscriptions.colNextBilling') }}
              <span class="sort-icon">{{ sortIcon('nextBilling') }}</span>
            </th>
            <th>{{ t('dashboard.subscriptions.colNotes') }}</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="sub in sortedSubscriptions" :key="sub.id">
            <td>{{ sub.name }}</td>
            <td>{{ formatCost(sub.amount) }}</td>
            <td>{{ sub.categoryName || '—' }}</td>
            <td><span class="badge">{{ t(`billingCycle.${sub.billingCycle}`) }}</span></td>
            <td>{{ formatDate(sub.nextBilling) }}</td>
            <td class="notes-cell">{{ sub.notes || '—' }}</td>
            <td class="actions-cell">
              <button class="btn-action btn-edit" :title="t('dashboard.subscriptions.actionEdit')" @click="emit('edit', sub)">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
                  <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
                </svg>
              </button>
              <button class="btn-action btn-deactivate" :title="t('dashboard.subscriptions.actionDeactivate')" @click="emit('deactivate', sub)">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <polyline points="3 6 5 6 21 6"/>
                  <path d="M19 6l-1 14a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2L5 6"/>
                  <path d="M10 11v6"/>
                  <path d="M14 11v6"/>
                  <path d="M9 6V4a1 1 0 0 1 1-1h4a1 1 0 0 1 1 1v2"/>
                </svg>
              </button>
            </td>
          </tr>
        </tbody>
      </table>

      <div v-else-if="isLoading" class="empty-state">
        <span class="spinner-dark"></span>
      </div>

      <div v-else class="empty-state">
        <p>{{ t('dashboard.subscriptions.empty') }}</p>
      </div>
    </div>
  </section>
</template>

<style scoped>
.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1rem;
}

.section-title {
  font-size: 1rem;
  font-weight: 600;
  color: #94a3b8;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  margin: 0;
}

.btn-add {
  display: inline-flex;
  align-items: center;
  gap: 0.4rem;
}

.btn-add svg {
  width: 14px;
  height: 14px;
}

.table-wrapper {
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 14px;
  overflow: hidden;
}

.table {
  width: 100%;
  border-collapse: collapse;
}

.table thead tr {
  background: rgba(255, 255, 255, 0.04);
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}

.th-sortable {
  cursor: pointer;
  user-select: none;
  white-space: nowrap;
}

.th-sortable:hover {
  color: #94a3b8;
}

.sort-icon {
  margin-left: 0.35rem;
  font-size: 0.7rem;
  opacity: 0.5;
}

.table th {
  padding: 0.75rem 1.25rem;
  text-align: left;
  font-size: 0.75rem;
  font-weight: 600;
  color: #64748b;
  text-transform: uppercase;
  letter-spacing: 0.06em;
}

.table td {
  padding: 1rem 1.25rem;
  font-size: 0.9rem;
  color: #e2e8f0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.table tbody tr:last-child td {
  border-bottom: none;
}

.table tbody tr:hover td {
  background: rgba(255, 255, 255, 0.03);
}

.badge {
  display: inline-block;
  padding: 0.2rem 0.65rem;
  background: rgba(99, 102, 241, 0.12);
  border: 1px solid rgba(99, 102, 241, 0.2);
  border-radius: 20px;
  font-size: 0.78rem;
  font-weight: 500;
  color: #a5b4fc;
}


.notes-cell {
  max-width: 220px;
  white-space: normal;
  word-break: break-word;
  color: #94a3b8;
}

.actions-cell {
  white-space: nowrap;
  vertical-align: middle;
  padding: 0.75rem 1.25rem;
}

.actions-cell > * + * {
  margin-left: 0.5rem;
}

</style>
