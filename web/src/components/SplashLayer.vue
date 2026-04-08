<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { NButton, NSpace } from 'naive-ui'
import { useI18n } from 'vue-i18n'
import { useBgm } from '../composables/useBgm'
import { useMobileLayout } from '@/composables/useMobileLayout'
import { withBasePublicPath } from '@/utils/assetUrls'
import LocaleSwitcher from '@/components/common/LocaleSwitcher.vue'

const emit = defineEmits<{
  (e: 'action', key: string): void
}>()

const { t } = useI18n()
const { isMobileLayout } = useMobileLayout()
const videoRef = ref<HTMLVideoElement | null>(null)
const splashPosterUrl = withBasePublicPath('assets/splash.png')
const splashVideoUrl = withBasePublicPath('assets/splash.mp4')

onMounted(() => {
  useBgm().play('splash')

  if (!videoRef.value) return

  const video = videoRef.value
  video.playbackRate = 0.8

  const handleTimeUpdate = () => {
    const duration = video.duration
    if (!duration) return

    const remaining = duration - video.currentTime
    if (remaining < 2 && remaining > 0) {
      const targetRate = 0.35 + (0.8 - 0.35) * (remaining / 2)
      video.playbackRate = targetRate
    }
  }

  video.addEventListener('timeupdate', handleTimeUpdate)
})

const menuOptions = computed(() => [
  { label: t('ui.start_game'), subLabel: 'Start Game', key: 'start', disabled: false },
  { label: t('ui.load_game'), subLabel: 'Load Game', key: 'load', disabled: false },
  { label: t('ui.achievements'), subLabel: 'Achievements', key: 'achievements', disabled: true },
  { label: t('ui.settings'), subLabel: 'Settings', key: 'settings', disabled: false },
  { label: t('ui.about'), subLabel: 'About', key: 'about', disabled: false },
  { label: t('ui.exit'), subLabel: 'Exit', key: 'exit', disabled: false },
])

function handleClick(key: string) {
  emit('action', key)
}
</script>

<template>
  <div class="splash-container" :class="{ 'splash-container--mobile': isMobileLayout }">
    <LocaleSwitcher variant="splash" :mobile="isMobileLayout" />
    <video
      ref="videoRef"
      class="splash-video"
      autoplay
      muted
      playsinline
      :poster="splashPosterUrl"
    >
      <source :src="splashVideoUrl" type="video/mp4" />
    </video>

    <div class="glass-panel">
      <div class="title-area">
        <h1>{{ t('splash.title') }}</h1>
        <p>AI Cultivation World Simulator</p>
      </div>

      <div class="menu-area">
        <n-space vertical :size="isMobileLayout ? 12 : 'large'">
          <n-button
            v-for="opt in menuOptions"
            :key="opt.key"
            size="large"
            block
            color="#ffffff20"
            text-color="#fff"
            class="menu-btn"
            :disabled="opt.disabled"
            v-sound="'click'"
            @click="handleClick(opt.key)"
          >
            <div class="btn-content">
              <span class="btn-label">{{ opt.label }}</span>
              <span class="btn-sub">{{ opt.subLabel }}</span>
            </div>
          </n-button>
        </n-space>
      </div>
    </div>
  </div>
</template>

<style scoped>
.splash-container {
  position: fixed;
  inset: 0;
  z-index: 500;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  background-color: #000;
  overflow: hidden;
  box-sizing: border-box;
}

.splash-container--mobile {
  justify-content: center;
  align-items: stretch;
  padding:
    max(16px, env(safe-area-inset-top))
    max(16px, env(safe-area-inset-right))
    max(16px, env(safe-area-inset-bottom))
    max(16px, env(safe-area-inset-left));
}

.splash-video {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  z-index: 0;
}

.glass-panel {
  position: relative;
  z-index: 1;
  width: 400px;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 0 60px;
  box-sizing: border-box;
  background: rgba(0, 0, 0, 0.4);
  border-right: 1px solid rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  box-shadow: 10px 0 30px rgba(0, 0, 0, 0.5);
}

.title-area {
  margin-bottom: 80px;
  color: #fff;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
}

.title-area h1 {
  margin: 0 0 10px;
  font-size: 3rem;
  font-weight: 700;
  letter-spacing: 4px;
}

.title-area p {
  margin: 0;
  font-size: 1.2rem;
  opacity: 0.8;
  letter-spacing: 2px;
}

.menu-btn {
  height: 60px;
  border: 1px solid rgba(255, 255, 255, 0.1);
  transition: all 0.3s ease;
  justify-content: flex-start;
  text-align: left;
  padding-left: 32px;
}

.menu-btn :deep(.n-button__content) {
  justify-content: flex-start;
  width: 100%;
}

.btn-content {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  width: 100%;
}

.btn-label {
  font-size: 20px;
  letter-spacing: 4px;
  line-height: 1.2;
}

.btn-sub {
  font-size: 10px;
  opacity: 0.6;
  letter-spacing: 1px;
  text-transform: uppercase;
}

.menu-btn:hover:not(:disabled) {
  background: rgba(255, 255, 255, 0.25);
  border-color: rgba(255, 255, 255, 0.5);
  transform: translateX(10px);
  box-shadow: 0 0 15px rgba(255, 255, 255, 0.1);
}

@media (max-width: 768px) {
  .splash-container--mobile .glass-panel {
    width: min(100%, 460px);
    height: 100%;
    margin: 0 auto;
    justify-content: flex-start;
    padding: 88px 20px 20px;
    border-right: none;
    border: 1px solid rgba(255, 255, 255, 0.12);
    border-radius: 28px;
    background: rgba(0, 0, 0, 0.58);
    box-shadow: 0 24px 60px rgba(0, 0, 0, 0.42);
    overflow-y: auto;
  }

  .splash-container--mobile .title-area {
    margin-bottom: 20px;
  }

  .splash-container--mobile .title-area h1 {
    font-size: clamp(2.2rem, 9vw, 3.4rem);
    line-height: 1.04;
    letter-spacing: 1px;
  }

  .splash-container--mobile .title-area p {
    font-size: 0.92rem;
    line-height: 1.25;
    letter-spacing: 0.5px;
    max-width: 9ch;
  }

  .splash-container--mobile .menu-area {
    margin-top: auto;
  }

  .splash-container--mobile .menu-btn {
    height: 56px;
    padding-left: 20px;
  }

  .splash-container--mobile .btn-label {
    font-size: 16px;
    letter-spacing: 2px;
  }

  .splash-container--mobile .btn-sub {
    font-size: 9px;
  }
}
</style>
