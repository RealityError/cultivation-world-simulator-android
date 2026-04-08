import { useMediaQuery } from '@vueuse/core'

export function useMobileLayout() {
  const isMobileLayout = useMediaQuery('(max-width: 960px)')

  return {
    isMobileLayout,
  }
}
