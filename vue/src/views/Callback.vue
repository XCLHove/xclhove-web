<script setup>
import WebHead from "@/components/WebHead.vue";
import {onMounted, ref} from "vue";
import {useRoute} from "vue-router";
import qs from "qs";
import axios from "axios";
import request from "@/utils/request";
import prompt from "@/utils/prompt";

const data = ref({
  client_id: '',
  client_secret: '',
  redirect_uri: '',
  code: '',
})
const token = ref({
  access_token: '',
  refresh_token: '',
})
const errorMessage = ref()

onMounted(() => {
  data.value.client_id = localStorage.getItem('client_id')
  data.value.client_secret = localStorage.getItem('client_secret')
  data.value.redirect_uri = localStorage.getItem('redirect_uri')
  data.value.code = useRoute().query.code ? useRoute().query.code : ''

  request.post('/oneDrives/token', data.value).then(result => {
    if (result.code !== '200') {
      errorMessage.value = result.message
      showError.value = true
      prompt(errorMessage.value, result.type, 3)
      return
    }
    token.value.access_token = result.data.token.access_token
    token.value.refresh_token = result.data.token.refresh_token

    localStorage.setItem('access_token', token.value.access_token)
    localStorage.setItem('refresh_token', token.value.refresh_token)
    localStorage.setItem('getTokenFinished', '1')

    const timer = setInterval(() => {
      if(localStorage.getItem('getTokenFinished') !== '1') {
        showPrompt.value = true
        //移移除定时器timer
        clearInterval(timer)
      }
    }, 1000)

  }).catch(error => {
    console.log(error)
  })
})

const showPrompt = ref(false)
const showError = ref(false)
</script>

<template>
  <div class="CallbackContainer">
    <WebHead>获取token</WebHead>
    <div class="token">
      <div class="accessToken">
        <div class="name">access_token</div>
        <el-input v-model="token.access_token"/>
      </div>
      <div class="refreshToken">
        <div class="name">refresh_token</div>
        <el-input v-model="token.refresh_token"/>
      </div>
      <div class="prompt">
        <div class="success" v-show="showPrompt">获取access_token与refresh_token成功，已自动填入，请返回添加存储源页面查看。</div>
        <div class="error" v-show="showError">获取access_token与refresh_token失败，原因：{{ errorMessage }},请返回添加存储源页面重新获取。</div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="less">
.CallbackContainer {
  width: 100%;
  height: 100vh;

  .token {
    width: 80%;
    margin: 0 auto;

    .accessToken, .refreshToken {
      margin: 10px 0;

      .name {
        margin: 3px 0;
      }
    }

    .prompt {
      .success {
        color: var(--color-vue);
      }
      .error {
        color: var(--color-danger);
      }
    }
  }
}
</style>