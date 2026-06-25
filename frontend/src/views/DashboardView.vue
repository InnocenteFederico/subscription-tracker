<script setup>
import { ref, onMounted, computed } from 'vue'
import { api } from '@/api/api'
import { useI18n } from 'vue-i18n'
import DashboardNavbar from '@/components/DashboardNavbar.vue'
import SubscriptionTable from '@/components/SubscriptionTable.vue'
import SubscriptionModal from '@/components/SubscriptionModal.vue'
import CategoryPieChart from '@/components/CategoryPieChart.vue'
import CategoryBarChart from '@/components/CategoryBarChart.vue'

const { t } = useI18n()

const subscriptions = ref([])
const categories = ref([])
const isLoading = ref(false)
const showModal = ref(false)
const modalRef = ref(null)
const summary = ref(null)

onMounted(async () => {
  isLoading.value = true
  try {
    const [subRes, catRes, sumRes] = await Promise.all([
      api.getSubscriptions({ params: { active: true } }),
      api.getCategories(),
      api.getSummary()
    ])
    subscriptions.value = subRes.data
    categories.value = catRes.data
    summary.value = sumRes.data
  } finally {
    isLoading.value = false
  }
})

function openAddModal() {
  modalRef.value?.open()
  showModal.value = true
}

function openEditModal(subscription) {
  modalRef.value?.open(subscription)
  showModal.value = true
}

function closeModal() {
  showModal.value = false
}

function onSubmitted(updatedList) {
  subscriptions.value = updatedList
  closeModal()
}

async function onDeactivate(subscription) {
  subscription.active = false
  const response = await api.updateSubscription(subscription)
  subscriptions.value = response.data
}

const pieChartParam = computed(() => {
  if (!summary.value) return null
  return {
    labels: summary.value.categories.map(c => c.name),
    data:   summary.value.categories.map(c => c.percentage),
    colors: summary.value.categories.map(c => c.color)
  }
})

const barChartParam = computed(() => {
  if (!summary.value) return null
  return {
    labels: summary.value.categories.map(c => c.name),
    data:   summary.value.categories.map(c => c.subtotal),
    colors: summary.value.categories.map(c => c.color)
  }
})
</script>

<template>
  <div class="app-page dashboard">

    <DashboardNavbar />

    <main class="content">

      <SubscriptionTable
        :subscriptions="subscriptions"
        :is-loading="isLoading"
        @add="openAddModal"
        @edit="openEditModal"
        @deactivate="onDeactivate"
      />

      <section class="section">
        <h2 class="section-title">{{ t('dashboard.stats.title') }}</h2>
        <div class="charts-grid">
          <CategoryPieChart
            v-if="pieChartParam"
            :title="t('dashboard.stats.byCategory')"
            :labels="pieChartParam.labels"
            :data="pieChartParam.data"
            :bg-color="pieChartParam.colors"
          />
          <CategoryBarChart
            v-if="barChartParam"
            :title="t('dashboard.stats.monthly')"
            :labels="barChartParam.labels"
            :data="barChartParam.data"
            :bg-color="barChartParam.colors"
          />
        </div>
      </section>

    </main>

    <SubscriptionModal
      ref="modalRef"
      :show="showModal"
      :categories="categories"
      @close="closeModal"
      @submitted="onSubmitted"
    />

  </div>
</template>

<style scoped>
.content {
  max-width: 960px;
  margin: 0 auto;
  padding: 2.5rem 1.5rem;
  display: flex;
  flex-direction: column;
  gap: 2.5rem;
}

.section-title {
  font-size: 1rem;
  font-weight: 600;
  color: #94a3b8;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  margin: 0 0 1rem;
}

.charts-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.25rem;
}

.chart-card--empty {
  background: rgba(255, 255, 255, 0.02);
  border: 1px dashed rgba(255, 255, 255, 0.08);
  border-radius: 14px;
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.chart-title {
  font-size: 0.78rem;
  font-weight: 600;
  color: #64748b;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  margin: 0;
}

.chart-placeholder-body {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
