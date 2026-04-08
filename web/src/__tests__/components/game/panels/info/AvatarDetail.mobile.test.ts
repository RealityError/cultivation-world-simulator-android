import { describe, expect, it } from 'vitest'
import { readFileSync } from 'node:fs'
import path from 'node:path'

describe('AvatarDetail mobile portrait affordance', () => {
  it('keeps portrait edit overlay visible on mobile layouts', () => {
    const source = readFileSync(
      path.resolve(
        __dirname,
        '../../../../../components/game/panels/info/AvatarDetail.vue',
      ),
      'utf-8',
    )

    expect(source).toMatch(
      /@media\s*\(max-width:\s*768px\)\s*\{[\s\S]*?\.portrait-overlay\s*\{\s*opacity:\s*1;\s*\}/,
    )
  })
})
