import { mount } from '@vue/test-utils'
import { describe, expect, it } from 'vitest'
import MobileEventDrawer from '@/components/game/panels/MobileEventDrawer.vue'
import { readFileSync } from 'node:fs'
import { resolve } from 'node:path'

const source = readFileSync(
  resolve(process.cwd(), 'src/components/game/panels/MobileEventDrawer.vue'),
  'utf-8',
)

describe('MobileEventDrawer', () => {
  it('emits close when scrim is clicked', async () => {
    const wrapper = mount(MobileEventDrawer, {
      props: {
        visible: true,
      },
      global: {
        stubs: {
          EventPanel: true,
          teleport: true,
        },
      },
    })

    await wrapper.get('[data-testid="mobile-event-scrim"]').trigger('click')

    expect(wrapper.emitted('close')).toHaveLength(1)
  })

  it('uses a flex sheet so the embedded event panel can scroll vertically on mobile', () => {
    expect(source).toMatch(
      /\.mobile-event-drawer__sheet\s*\{[\s\S]*?display:\s*flex;[\s\S]*?flex-direction:\s*column;/,
    )
  })
})
