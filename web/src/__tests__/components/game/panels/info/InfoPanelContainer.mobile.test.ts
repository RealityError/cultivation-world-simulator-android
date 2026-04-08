import { mount } from '@vue/test-utils'
import { createPinia } from 'pinia'
import { createI18n } from 'vue-i18n'
import { describe, expect, it } from 'vitest'
import { useUiStore } from '@/stores/ui'
import InfoPanelContainer from '@/components/game/panels/info/InfoPanelContainer.vue'

describe('InfoPanelContainer mobile', () => {
  it('renders mobile class when mobile prop is true', async () => {
    const i18n = createI18n({
      legacy: false,
      locale: 'zh-CN',
      messages: {},
    })

    const wrapper = mount(InfoPanelContainer, {
      props: {
        mobile: true,
      },
      global: {
        plugins: [createPinia(), i18n],
        stubs: {
          AvatarDetail: true,
          SectDetail: true,
          RegionDetail: true,
        },
      },
    })

    const uiStore = useUiStore()
    uiStore.selectedTarget = { type: 'avatar', id: 'avatar-1' }
    uiStore.detailData = { id: 'avatar-1', name: '测试角色' } as any
    await wrapper.vm.$nextTick()

    expect(wrapper.get('.info-panel').classes()).toContain('info-panel--mobile')
  })
})
