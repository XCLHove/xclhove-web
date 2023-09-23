<script setup>
import {ref} from "vue";
import sleep from "@/utils/sleep";

const pageLoad = ref(true)
const pageShow = ref(false)
const pageInit = async () => {
  await sleep(0.5).then(() => {
    pageLoad.value = false
  })
  pageShow.value = !pageShow.value
}
pageInit()
</script>

<template>
  <div class="loadContainer" v-loading="pageLoad">
    <Transition>
      <div class="showContainer" v-show="pageShow">
        <slot></slot>
      </div>
    </Transition>
  </div>
</template>

<style scoped lang="less">
.loadContainer {
  width: 100vw;
  height: 100vh;
}
</style>