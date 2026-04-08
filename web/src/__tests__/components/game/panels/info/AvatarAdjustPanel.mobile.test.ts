import { describe, expect, it } from 'vitest'
import { readFileSync } from 'node:fs'
import { resolve } from 'node:path'

const source = readFileSync(
  resolve(process.cwd(), 'src/components/game/panels/info/components/AvatarAdjustPanel.vue'),
  'utf-8',
)

describe('AvatarAdjustPanel mobile layout', () => {
  it('adds a mobile override that keeps the panel inside the viewport', () => {
    expect(source).toMatch(
      /@media\s*\(max-width:\s*768px\)[\s\S]*?\.adjust-panel\s*\{[\s\S]*?left:\s*12px;[\s\S]*?right:\s*12px;[\s\S]*?top:\s*max\(12px,\s*env\(safe-area-inset-top\)\);[\s\S]*?bottom:\s*max\(12px,\s*env\(safe-area-inset-bottom\)\);/,
    )
    expect(source).toMatch(
      /@media\s*\(max-width:\s*768px\)[\s\S]*?\.adjust-panel\s*\{[\s\S]*?width:\s*auto;[\s\S]*?max-width:\s*none;/,
    )
  })
})
