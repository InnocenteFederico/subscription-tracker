<template>
  <div class="cost-preview">
    <h2>Monthly Cost Preview</h2>
    <p>Estimated cost: {{ estimatedCost }}</p>
  </div>

  <div class="cost-preview">
    <h2>Annual Cost Preview</h2>
    <p>Estimated cost: {{ estimatedAnnualCost.toFixed(2) }}</p>
  </div>

  <button @click="increaseCost">Increase Cost</button>
</template>

<script setup>
import { ref, computed } from 'vue'

const estimatedCost = ref(9.99)
const estimatedAnnualCost = computed(() => estimatedCost.value * 12)

const increaseCost = () => {
  estimatedCost.value += 1
}
</script>
