import { afterEach, describe, expect, it, vi } from 'vitest'
import { useMobileLayout } from '@/composables/useMobileLayout'

describe('useMobileLayout', () => {
  afterEach(() => {
    vi.unstubAllGlobals()
  })

  it('returns true when media query matches', () => {
    vi.stubGlobal('matchMedia', vi.fn().mockReturnValue({
      matches: true,
      addEventListener: vi.fn(),
      removeEventListener: vi.fn(),
    }))

    const { isMobileLayout } = useMobileLayout()

    expect(isMobileLayout.value).toBe(true)
  })
})
