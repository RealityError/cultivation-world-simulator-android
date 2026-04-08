import { describe, expect, it } from 'vitest'
import { readFileSync } from 'node:fs'
import { resolve } from 'node:path'

const appSource = readFileSync(
  resolve(process.cwd(), 'src/App.vue'),
  'utf-8',
)

describe('App mobile shell styles', () => {
  it('includes mobile-safe top control styles for touch targets and safe area spacing', () => {
    expect(appSource).toMatch(
      /@media\s*\(max-width:\s*768px\)[\s\S]*?\.top-controls\s*\{[\s\S]*?top:\s*max\(12px,\s*env\(safe-area-inset-top\)\);[\s\S]*?right:\s*max\(12px,\s*env\(safe-area-inset-right\)\);/,
    )
    expect(appSource).toMatch(
      /@media\s*\(max-width:\s*768px\)[\s\S]*?\.control-btn\s*\{[\s\S]*?width:\s*44px;[\s\S]*?height:\s*44px;/,
    )
  })
})
