<script setup>
import WebHead from "@/components/WebHead.vue";
import BeiAnHao from "@/components/BeiAnHao.vue";
import {onUnmounted, ref} from "vue";
import prompt from "@/utils/prompt";
import request from "@/utils/request";
import router from "@/router/router";

const storage = ref({
  name: '',
  client_id: '',
  client_secret: '',
  redirect_uri: '',
  access_token: '',
  refresh_token: '',
})
const storageInfoRef = ref()
const storageInfo = ref({
  rules: ref({
    storageName: [
      {required: true, message: '必填', trigger: 'blur'}
    ],
    client_id: [
      {required: true, message: '必填', trigger: 'blur'}
    ],
    client_secret: [
      {required: true, message: '必填', trigger: 'blur'}
    ],
    redirect_uri: [
      {required: true, message: '必填', trigger: 'blur'}
    ],
    access_token: [
      {required: true, message: '必填', trigger: 'blur'}
    ],
    refresh_token: [
      {required: true, message: '必填', trigger: 'blur'}
    ],
  }),
  getToken: () => {
    localStorage.setItem('client_id', storage.value.client_id)
    localStorage.setItem('client_secret', storage.value.client_secret)
    localStorage.setItem('redirect_uri', storage.value.redirect_uri)
    const url = 'https://login.microsoftonline.com/common/oauth2/v2.0/authorize?' +
        `client_id=${storage.value.client_id}` +
        '&scope=files.readwrite.all offline_access' +
        '&response_type=code' +
        `&redirect_uri=${storage.value.redirect_uri}`
    window.open(url, '_target')
    localStorage.setItem('getTokenFinished', '0')
    const timer = setInterval(() => {
      if(localStorage.getItem('getTokenFinished') === '1') {
        storage.value.access_token = localStorage.getItem('access_token')
        storage.value.refresh_token = localStorage.getItem('refresh_token')
        showPrompt.value = true
        localStorage.clear()
        //移移除定时器timer
        clearInterval(timer)
      }
    }, 1000)
  },
  add: () => {
    request.post('/oneDrives', storage.value).then(result => {
      prompt(result.message, result.type)
      //router.push('')
    })
  }
})

onUnmounted(() => {
  localStorage.clear()
})

const showPrompt = ref(false)
</script>

<template>
  <div class="addStorageContainer">
    <!-- WebHead -->
    <WebHead>添加存储源</WebHead>
    <!-- storageInfo -->
    <div class="storageInfo">
      <el-form
          ref="storageInfoRef"
          status-icon
          :model="storage"
          :rules="storageInfo.rules"
      >
        <div class="infoItem">
          <div class="infoName">储存源名</div>
          <el-form-item prop="storageName">
            <el-input v-model.trim="storage.name"/>
          </el-form-item>
        </div>
        <div class="infoItem">
          <div class="infoName">client_id</div>
          <el-form-item prop="client_id">
            <el-input v-model.trim="storage.client_id"/>
          </el-form-item>
        </div>
        <div class="infoItem">
          <div class="infoName">client_secret</div>
          <el-form-item prop="client_secret">
            <el-input v-model.trim="storage.client_secret"/>
          </el-form-item>
        </div>
        <div class="infoItem">
          <div class="infoName">redirect_uri</div>
          <el-form-item prop="redirect_uri">
            <el-input v-model.trim="storage.redirect_uri"/>
          </el-form-item>
        </div>
        <div class="getToken">
          <span class="toGet" @click="storageInfo.getToken()">获取access_token和refresh_token</span>
          <span class="prompt" v-show="showPrompt">获取成功，已自动填入。</span>
        </div>
        <div class="infoItem">
          <div class="infoName">access_token</div>
          <el-form-item prop="access_token">
            <el-input v-model.trim="storage.access_token"/>
          </el-form-item>
        </div>
        <div class="infoItem">
          <div class="infoName">refresh_token</div>
          <el-form-item prop="refresh_token">
            <el-input v-model.trim="storage.refresh_token"/>
          </el-form-item>
        </div>
      </el-form>
      <button @click="storageInfo.add()">添加</button>
    </div>
    <!-- 备案号 -->
    <BeiAnHao></BeiAnHao>
  </div>
</template>

<style scoped lang="less">
.addStorageContainer {
  width: 100%;
  height: 100vh;

  .storageInfo {
    width: 80%;
    margin: 0 auto;

    .infoItem {
      margin: 10px 0;

      .infoName {
        margin: 10px 0;
      }
    }

    .getToken {
      .toGet {
        color: var(--color-primary);
      }

      .toGet:hover {
        opacity: 0.5;
      }

      .prompt {
        color: var(--color-vue);
        margin: 0 10px;
      }
    }

    button {
      background: white;
      font-size: 30px;
      padding: 5px 0;
      border: 1px solid #dcdfe6;
      border-radius: 5px;
      width: 100%;
      margin: 30px 0;
    }

    button:hover {
      color: var(--color-vue);
    }
  }
}
</style>