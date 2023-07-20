<template>
  <RouterView/>
  <!--备案号-->
  <BeiAnHao/>
</template>

<style lang="less" scoped>

</style>

<script>
//导入备案号组件
import BeiAnHao from "@/components/BeiAnHao.vue"
export default {
  components: {
    BeiAnHao
  }
}
</script>