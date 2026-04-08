import { watch, type Ref } from 'vue'
import { storeToRefs } from 'pinia'
import { useUiStore } from '@/stores/ui'
import { useSystemStore } from '@/stores/system'
import { logError } from '@/utils/appError'

interface UseGameControlOptions {
  gameInitialized: Ref<boolean>
  showMenu: Ref<boolean>
  canCloseMenu: Ref<boolean>
  openGameMenu: () => void
  closeMenu: () => void
}

export function useGameControl(options: UseGameControlOptions) {
  const uiStore = useUiStore()
  const systemStore = useSystemStore()
  const { isManualPaused } = storeToRefs(systemStore)

  watch(options.showMenu, (menuVisible) => {
    if (!options.gameInitialized.value) return

    if (menuVisible) {
      systemStore.pause().catch((e) => logError('GameControl pause', e))
      return
    }

    if (!isManualPaused.value) {
      systemStore.resume().catch((e) => logError('GameControl resume', e))
    }
  })

  function canHandleBackAction() {
    if (uiStore.selectedTarget) return true
    if (uiStore.mobileEventPanelOpen) return true
    if (options.showMenu.value) return options.canCloseMenu.value
    return options.gameInitialized.value
  }

  function handleBackAction() {
    if (uiStore.selectedTarget) {
      uiStore.clearSelection()
      return true
    }

    if (uiStore.mobileEventPanelOpen) {
      uiStore.closeMobileEventPanel()
      return true
    }

    if (options.showMenu.value) {
      if (options.canCloseMenu.value) {
        options.closeMenu()
        return true
      }

      return false
    }

    if (options.gameInitialized.value) {
      options.openGameMenu()
      return true
    }

    return false
  }

  function handleKeydown(e: KeyboardEvent) {
    if (e.key === 'Escape') {
      handleBackAction()
    }
  }

  function toggleManualPause() {
    systemStore.togglePause()
  }

  return {
    isManualPaused,
    canHandleBackAction,
    handleBackAction,
    handleKeydown,
    toggleManualPause,
  }
}
