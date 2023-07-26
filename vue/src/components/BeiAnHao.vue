<template>
  <!--  备案号 -->
  <div class="beiAnHao">
    <a href="https://beian.miit.gov.cn" target="_blank">{{ beiAnHao }}</a>
  </div>
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

<script>
import config from "@/utils/config.js"

export default {
  computed: {
    beiAnHao() {
      return config.beiAnHao
    },
  },
}
</script>