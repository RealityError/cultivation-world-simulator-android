import { onMounted, onUnmounted } from 'vue'

declare global {
  interface Window {
    __cwsCanHandleBack?: () => boolean
    __cwsHandleBack?: () => void
  }
}

export function useAndroidBackBridge(
  canHandleBack: () => boolean,
  handleBack: () => void,
) {
  onMounted(() => {
    window.__cwsCanHandleBack = canHandleBack
    window.__cwsHandleBack = handleBack
  })

  onUnmounted(() => {
    if (window.__cwsCanHandleBack === canHandleBack) {
      delete window.__cwsCanHandleBack
    }

    if (window.__cwsHandleBack === handleBack) {
      delete window.__cwsHandleBack
    }
  })
}
