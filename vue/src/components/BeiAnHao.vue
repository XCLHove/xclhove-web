<script setup>
import config from "@/utils/config.js"
import {onMounted, ref, watch} from "vue";

const show =ref(true)
const height = ref()
watch(height, () => {
  if (height.value < 520) {
    show.value = false
    return
  }
  show.value = true
})
window.onresize = () => {
  height.value = document.body.clientHeight
}

const pageInit = () => {
  height.value = document.body.clientHeight
}
onMounted(() => {
  pageInit()
})
</script>

<template>
  <!--  备案号 -->
  <Transition>
    <div class="beiAnHao" v-show="show">
      <a href="https://beian.miit.gov.cn" target="_blank">{{ config.beiAnHao }}</a>
    </div>
  </Transition>
</template>

<style lang="less" scoped>
@import "@/assets/main";

.beiAnHao {
  position: absolute;
  bottom: 0;
  width: 100%;
  padding: 5px 0;
  .flex();
  justify-content: center;

  a {
    text-decoration: none;
    color: #909bb7;
  }
}
</style>