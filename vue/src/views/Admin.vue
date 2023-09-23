<script setup>
import {onMounted, ref} from "vue";
import request from "@/utils/request";
import {useRoute} from "vue-router";
import axios from "axios";
import qs from "qs";
import prompt from "@/utils/prompt";

const data = ref({
  client_id: '418e010d-c619-464d-b088-a6ed4b37cd50',
  scope: 'files.readwrite.all offline_access',
  redirect_uri: 'http://localhost/admin',
})

const getCode = async () => {
  const url = 'https://login.microsoftonline.com/common/oauth2/v2.0/authorize?' +
      `client_id=${data.value.client_id}` +
      `&scope=${data.value.scope}` +
      '&response_type=code' +
      `&redirect_uri=${data.value.redirect_uri}`
  window.open(url, '_target')
}

const data2 = {
  'client_id': '418e010d-c619-464d-b088-a6ed4b37cd50',
  'client_secret': 'o008Q~u0cz5AatEOigeCpoVnA7NsmtvdP1q7iaTx',
  'redirect_uri': 'http://localhost/admin',
  'code': '',
  'grant_type': 'authorization_code'
}
const url = 'https://login.microsoftonline.com/common/oauth2/v2.0/token'
const getToken = () => {
  const options = {
    method: 'POST',
    headers: { 'content-type': 'application/x-www-form-urlencoded' },
    data: qs.stringify(data2),
    url,
  }
  axios(options
  ).then(result => {
    console.log(result)
  }).catch(error => {
    console.log('error:',error)
  })
}

onMounted(() => {
  const code = useRoute().query.code ? useRoute().query.code : ''
  if (code !== '') {
    data2.code = code
    console.log(data2.code)
    getToken()
  }
})
</script>

<template>
  <div class="adminContainer">
    <h1 style="text-align: center">admin</h1>
    <el-button type="primary" @click="getCode()">getCode</el-button>
  </div>
</template>

<style scoped lang="less">
.adminContainer {
  text-align: center;
}
</style>