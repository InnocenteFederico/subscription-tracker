<script setup>
import Chart from 'chart.js/auto'
import { ref, onMounted, onBeforeUnmount } from 'vue'

const props = defineProps({
  title: { type: String, default: '' },
  labels: { type: Array, default: () => [] },
  data: { type: Array, default: () => [] },
  bgColor: { type: Array, default: () => [] }
})

const canvas = ref(null)
let chartInstance = null

onMounted(() => {
  chartInstance = new Chart(canvas.value, {
    type: 'pie',
    data: {
      labels: props.labels,
      datasets: [{
        data: props.data,
        backgroundColor: props.bgColor,
        borderWidth: 2,
        borderColor: '#0f172a'
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: true,
      plugins: {
        legend: {
          position: 'bottom',
          labels: {
            color: '#94a3b8',
            font: { family: 'Inter, system-ui, sans-serif', size: 12 },
            padding: 16,
            usePointStyle: true,
            pointStyleWidth: 10
          }
        },
        tooltip: {
          backgroundColor: 'rgba(15, 23, 42, 0.95)',
          titleColor: '#f1f5f9',
          bodyColor: '#94a3b8',
          borderColor: 'rgba(255,255,255,0.08)',
          borderWidth: 1,
          padding: 10
        }
      }
    }
  })
})

onBeforeUnmount(() => {
  chartInstance?.destroy()
})
</script>

<template>
  <div class="chart-card">
    <p v-if="title" class="chart-title">{{ title }}</p>
    <div class="chart-body">
      <canvas ref="canvas"></canvas>
    </div>
  </div>
</template>

<style scoped>
.chart-card {
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.08);
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

.chart-body {
  max-width: 300px;
  margin: 0 auto;
  width: 100%;
}
</style>
