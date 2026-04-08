import { describe, expect, it } from 'vitest'
import { readFileSync } from 'node:fs'
import { resolve } from 'node:path'

const indexHtmlSource = readFileSync(
  resolve(process.cwd(), 'index.html'),
  'utf-8',
)

describe('index.html mobile viewport', () => {
  it('enables viewport-fit=cover for safe-area CSS on mobile webviews', () => {
    expect(indexHtmlSource).toContain('viewport-fit=cover')
  })
})
