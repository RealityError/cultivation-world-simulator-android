import { mount } from '@vue/test-utils'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import { readFileSync } from 'node:fs'
import { resolve } from 'node:path'
import SplashLayer from '@/components/SplashLayer.vue'
import { createI18n } from 'vue-i18n'

vi.mock('@/composables/useBgm', () => ({
  useBgm: () => ({
    play: vi.fn(),
  }),
}))

const splashLayerSource = readFileSync(
  resolve(process.cwd(), 'src/components/SplashLayer.vue'),
  'utf-8',
)

describe('SplashLayer', () => {
  beforeEach(() => {
    window.sessionStorage.clear()
  })

  it('should render successfully', () => {
    const i18n = createI18n({
      legacy: false,
      locale: 'zh-CN',
      messages: {
        'zh-CN': {
          splash: {
            title: 'Title',
            subtitle: 'Subtitle',
            click_to_start: 'Click to start'
          },
          ui: {
            start_game: 'Start',
            load_game: 'Load',
            achievements: 'Achievements',
            settings: 'Settings',
            about: 'About',
            exit: 'Exit',
            language_switcher_button: 'Language',
            language_switcher_hint: 'Choose your display language',
          }
        }
      }
    })

    const wrapper = mount(SplashLayer, {
      global: {
        plugins: [i18n],
        directives: {
          sound: () => {},
        },
      }
    })

    expect(wrapper.exists()).toBe(true)
    expect(wrapper.find('.splash-container').exists()).toBe(true)
    expect(wrapper.find('.locale-trigger--splash').exists()).toBe(true)
  })

  it('adds mobile layout class when viewport matches mobile breakpoint', () => {
    vi.stubGlobal('matchMedia', vi.fn().mockReturnValue({
      matches: true,
      addEventListener: vi.fn(),
      removeEventListener: vi.fn(),
    }))

    const i18n = createI18n({
      legacy: false,
      locale: 'zh-CN',
      messages: {
        'zh-CN': {
          splash: {
            title: 'Title',
          },
          ui: {
            start_game: 'Start',
            load_game: 'Load',
            achievements: 'Achievements',
            settings: 'Settings',
            about: 'About',
            exit: 'Exit',
            language_switcher_button: 'Language',
            language_switcher_hint: 'Choose your display language',
          }
        }
      }
    })

    const wrapper = mount(SplashLayer, {
      global: {
        plugins: [i18n],
        directives: {
          sound: () => {},
        },
      }
    })

    expect(wrapper.find('.splash-container').classes()).toContain('splash-container--mobile')
    expect(wrapper.find('.locale-switcher').classes()).toContain('locale-switcher--mobile')
  })

  it('includes compact mobile spacing so the full splash menu can fit without vertical squeezing', () => {
    expect(splashLayerSource).toMatch(
      /@media\s*\(max-width:\s*768px\)[\s\S]*?\.splash-container--mobile \.glass-panel\s*\{[\s\S]*?padding:\s*88px 20px 20px;/,
    )
    expect(splashLayerSource).toMatch(
      /@media\s*\(max-width:\s*768px\)[\s\S]*?\.splash-container--mobile \.title-area h1\s*\{[\s\S]*?font-size:\s*clamp\(2\.2rem,\s*9vw,\s*3\.4rem\);/,
    )
    expect(splashLayerSource).toMatch(
      /@media\s*\(max-width:\s*768px\)[\s\S]*?\.splash-container--mobile \.menu-btn\s*\{[\s\S]*?height:\s*56px;[\s\S]*?padding-left:\s*20px;/,
    )
  })
})
