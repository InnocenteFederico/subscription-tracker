<script setup>
const model = defineModel({ required: true })

defineProps({
  label: { type: String, required: true },
  id: { type: String, required: true },
  type: { type: String, default: 'text' },
  placeholder: { type: String, default: '' },
  autocomplete: { type: String, default: 'off' },
  error: { type: String, default: '' }
})
</script>

<template>
  <div class="field">
    <label :for="id">{{ label }}</label>
    <input
      :id="id"
      v-model="model"
      :type="type"
      :placeholder="placeholder"
      :autocomplete="autocomplete"
      :class="{ 'input-error': error }"
      required
    />
    <Transition name="hint">
      <span v-if="error" class="hint-error">{{ error }}</span>
    </Transition>
  </div>
</template>

<style scoped>
.field input.input-error {
  border-color: #f87171;
  box-shadow: 0 0 0 3px rgba(248, 113, 113, 0.2);
}

.hint-error {
  font-size: 0.78rem;
  color: #f87171;
}

.hint-enter-active,
.hint-leave-active {
  transition: opacity 0.15s, transform 0.15s;
}
.hint-enter-from,
.hint-leave-to {
  opacity: 0;
  transform: translateY(-3px);
}
</style>
