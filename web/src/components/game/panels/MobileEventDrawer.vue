<script setup lang="ts">
import EventPanel from './EventPanel.vue'

defineProps<{
  visible: boolean
}>()

const emit = defineEmits<{
  (e: 'close'): void
}>()
</script>

<template>
  <teleport to="body">
    <div v-if="visible" class="mobile-event-drawer">
      <div
        class="mobile-event-drawer__scrim"
        data-testid="mobile-event-scrim"
        @click="emit('close')"
      ></div>
      <section class="mobile-event-drawer__sheet">
        <header class="mobile-event-drawer__header">
          <button
            class="mobile-event-drawer__close"
            type="button"
            @click="emit('close')"
          >
            关闭
          </button>
        </header>
        <EventPanel :mobile="true" />
      </section>
    </div>
  </teleport>
</template>

<style scoped>
.mobile-event-drawer {
  position: fixed;
  inset: 0;
  z-index: 120;
}

.mobile-event-drawer__scrim {
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.5);
}

.mobile-event-drawer__sheet {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;
  height: min(78vh, 720px);
  display: flex;
  flex-direction: column;
  background: #181818;
  border-top: 1px solid #333;
  border-top-left-radius: 16px;
  border-top-right-radius: 16px;
  overflow: hidden;
}

.mobile-event-drawer__header {
  display: flex;
  justify-content: flex-end;
  padding: 12px 16px 0;
  flex-shrink: 0;
}

.mobile-event-drawer__close {
  background: transparent;
  color: #fff;
  border: 1px solid #555;
  border-radius: 999px;
  padding: 6px 12px;
}

:deep(.sidebar-section--mobile) {
  flex: 1;
  min-height: 0;
}
</style>
