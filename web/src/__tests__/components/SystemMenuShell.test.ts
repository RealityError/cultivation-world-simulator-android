import { describe, expect, it } from 'vitest'
import { mount } from '@vue/test-utils'
import { readFileSync } from 'node:fs'
import { resolve } from 'node:path'

import SystemMenuShell from '@/components/SystemMenuShell.vue'
import { createTestI18n, testDefaultLocale } from '@/__tests__/utils/i18n'

const i18n = createTestI18n(
  {
    ui: {
      system_menu_title: 'System Menu',
      start_game: 'Start Game',
      load_game: 'Load Game',
      save_game: 'Save Game',
      create_character: 'Create Character',
      delete_character: 'Delete Character',
      llm_settings: 'LLM Settings',
      settings: 'Settings',
      about: 'About',
      other: 'Other',
    },
  },
  testDefaultLocale,
)

const systemMenuShellSource = readFileSync(
  resolve(process.cwd(), 'src/components/SystemMenuShell.vue'),
  'utf-8',
)

describe('SystemMenuShell', () => {
  it('renders system menu tabs and disables game-only tabs before initialization', () => {
    const wrapper = mount(SystemMenuShell, {
      props: {
        visible: true,
        activeTab: 'start',
        gameInitialized: false,
      },
      global: {
        plugins: [i18n],
        directives: {
          sound: () => {},
        },
      },
    })

    const tabs = wrapper.findAll('.menu-tabs button')

    expect(wrapper.find('.system-menu-overlay').exists()).toBe(true)
    expect(tabs).toHaveLength(9)
    expect(tabs[0]?.text()).toBe('Start Game')
    expect(tabs[2]?.attributes('disabled')).toBeDefined()
    expect(tabs[3]?.attributes('disabled')).toBeDefined()
    expect(tabs[4]?.attributes('disabled')).toBeDefined()
  })

  it('includes mobile tab styles that avoid evenly squeezing every tab button', () => {
    expect(systemMenuShellSource).toMatch(
      /@media\s*\(max-width:\s*768px\)[\s\S]*?\.menu-tabs\s*\{[\s\S]*?overflow-x:\s*auto;[\s\S]*?overflow-y:\s*hidden;[\s\S]*?justify-content:\s*flex-start;[\s\S]*?flex-wrap:\s*nowrap;/,
    )
    expect(systemMenuShellSource).toMatch(
      /@media\s*\(max-width:\s*768px\)[\s\S]*?\.menu-tabs button\s*\{[\s\S]*?flex:\s*0 0 auto;[\s\S]*?white-space:\s*nowrap;/,
    )
  })
})
