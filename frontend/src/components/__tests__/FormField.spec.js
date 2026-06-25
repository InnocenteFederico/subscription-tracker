import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import FormField from '../FormField.vue'

function mountField(props = {}) {
  return mount(FormField, {
    props: { label: 'Email', id: 'email', modelValue: '', ...props },
    global: { stubs: { Transition: false } },
  })
}

describe('FormField', () => {
  it('mostra la label e il campo input', () => {
    const wrapper = mountField()
    expect(wrapper.find('label').text()).toBe('Email')
    expect(wrapper.find('input').exists()).toBe(true)
  })

  it('la label è associata all\'input tramite id', () => {
    const wrapper = mountField({ id: 'test-id' })
    expect(wrapper.find('label').attributes('for')).toBe('test-id')
    expect(wrapper.find('input').attributes('id')).toBe('test-id')
  })

  it('applica il type corretto all\'input', () => {
    const wrapper = mountField({ type: 'password' })
    expect(wrapper.find('input').attributes('type')).toBe('password')
  })

  it('applica il placeholder corretto', () => {
    const wrapper = mountField({ placeholder: 'Inserisci email' })
    expect(wrapper.find('input').attributes('placeholder')).toBe('Inserisci email')
  })

  it('emette update:modelValue quando l\'utente scrive', async () => {
    const wrapper = mountField()
    await wrapper.find('input').setValue('test@test.com')
    expect(wrapper.emitted('update:modelValue')).toBeTruthy()
    expect(wrapper.emitted('update:modelValue')[0]).toEqual(['test@test.com'])
  })

  it('aggiunge la classe input-error se error è valorizzato', () => {
    const wrapper = mountField({ error: 'Campo obbligatorio' })
    expect(wrapper.find('input').classes()).toContain('input-error')
  })

  it('non aggiunge la classe input-error se error è vuoto', () => {
    const wrapper = mountField({ error: '' })
    expect(wrapper.find('input').classes()).not.toContain('input-error')
  })

  it('mostra il messaggio di errore quando error è valorizzato', () => {
    const wrapper = mountField({ error: 'Campo obbligatorio' })
    expect(wrapper.find('.hint-error').text()).toBe('Campo obbligatorio')
  })
})
