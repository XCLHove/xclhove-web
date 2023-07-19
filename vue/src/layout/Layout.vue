<template>
  <RouterView/>
  <!--    备案号    -->
  <div class="beiAnHao">
    <a href="https://beian.miit.gov.cn" target="_blank">{{ beiAnHao }}</a>
  </div>
</template>

<style scoped>
.beiAnHao {
  position: absolute;
  bottom: 0;
  text-align: center;
  width: calc(100% - 5px);
}

.beiAnHao a {
  text-decoration: none;
  color: #909bb7;
  display: block;
  margin: 0 auto;
}
</style>

<script>
import config from "@/utils/config.js"
export default {
  data() {
    return {
      beiAnHao: config.beiAnHao,
    }
  },
}
</script>