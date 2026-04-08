import { mount } from '@vue/test-utils'
import { afterEach, describe, expect, it, vi } from 'vitest'
import { defineComponent } from 'vue'
import { useAndroidBackBridge } from '@/composables/useAndroidBackBridge'

describe('useAndroidBackBridge', () => {
  afterEach(() => {
    delete window.__cwsCanHandleBack
    delete window.__cwsHandleBack
  })

  it('registers and unregisters back handlers on lifecycle', () => {
    const canHandle = vi.fn(() => true)
    const handle = vi.fn()

    const wrapper = mount(defineComponent({
      setup() {
        useAndroidBackBridge(canHandle, handle)
        return {}
      },
      template: '<div />',
    }))

    expect(window.__cwsCanHandleBack?.()).toBe(true)
    window.__cwsHandleBack?.()
    expect(handle).toHaveBeenCalledTimes(1)

    wrapper.unmount()

    expect(window.__cwsCanHandleBack).toBeUndefined()
    expect(window.__cwsHandleBack).toBeUndefined()
  })
})
