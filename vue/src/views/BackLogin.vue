<script setup>
import {ref} from "vue";
import prompt from "@/utils/prompt";
import request from "@/utils/request";
import router from "@/router/router";

const loginFormRef = ref()
const validate = (rule, value, callback) => {
  const pattern = /[^[a-z0-9A-Z\.*_]+/g
  if (!pattern.test(value)) {
    return true
  }
  return callback(new Error('仅支持"英文字母"、"数字"、"."、"*"和"_"'))
}
const loginFormRules = ref({
  backAccount: [
    {required: true, message: '请输入账号', trigger: 'blur'},
    {min: 1, max: 20, message: '账号长度为1-20位', trigger: 'blur'},
    {validator: validate, trigger: 'blur'},
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
    {min: 5, max: 20, message: '密码长度为5-20位', trigger: 'blur'},
    {validator: validate, trigger: 'blur'},
  ],
})
const admin = ref({
  backAccount: '',
  password: '',
})

const login = () => {
  loginFormRef.value.validate(valid => {
    if (valid) {
      request.post(
          "/system/backLogin",
          admin.value
      ).then(result => {
        prompt(result.message, result.type)
        localStorage.setItem('token', result.data.token)
        router.push('/admin')
      })
    }
  })
}

const pageInit = async () => {
  const result = await request.get("/system/statuses")
  const systemStatuses = result.data.systemStatuses
  const installedConfig = systemStatuses.filter(status => status.name === 'installed')[0]

  if (installedConfig.value === 0) {
    await router.push('/init')
    return
  }

  if (localStorage.getItem('token') !== null) {
    await router.push('/admin')
  }
}

pageInit()
</script>

<template>
  <Load>
    <div class="backLoginContainer">
      <Head>后台管理</Head>
      <div class="backLogin">
        <div class="part logo">
          <img src="../image/logo/XCLHove-logo.png" alt="logo" title="XCLHove-logo" height="400" width="400"
               @click="router.push('/')">
        </div>
        <div class="part login">
          <img src="/favicon.ico" alt="XCLHove-icon" title="XCLHove-icon" width="30" height="30">
          <div class="title">登录</div>
          <el-form ref="loginFormRef" :model="admin" :rules="loginFormRules" :inline-message="true" status-icon>
            <el-form-item prop="backAccount">
              <el-input ref="backAccountRef" v-model.trim="admin.backAccount" placeholder="账号" prefix-icon="User"
                        @keyup.enter="$refs.passwordRef.focus()"
                        @keyup.up="$refs.passwordRef.focus()"
                        @keyup.down="$refs.passwordRef.focus()"/>
            </el-form-item>
            <el-form-item prop="password">
              <el-input ref="passwordRef" v-model.trim="admin.password" placeholder="密码" show-password
                        prefix-icon="Lock"
                        @keyup.enter="login()"
                        @keyup.up="$refs.backAccountRef.focus()"
                        @keyup.down="$refs.backAccountRef.focus()"/>
            </el-form-item>
            <el-button type="primary" class="width100" @click="login()">登录</el-button>
          </el-form>
        </div>
      </div>
    </div>
  </Load>
</template>

<style scoped lang="less">
@import "@/assets/main";

.backLoginContainer {
  width: 100%;
  height: 100vh;

  .backLogin {
    width: 800px;
    height: 400px;
    border: var(--color-lightGray) solid 1px;
    border-radius: 10px;
    margin: 150px auto;
    .flex();
    flex-direction: row;
    overflow: hidden;

    .part {
      width: 400px;
      height: 100%;
    }

    .logo {
      border-right: var(--color-lightGray) solid 1px;
    }

    @media screen and (max-width: 800px) {
      width: 400px;

      .logo {
        display: none;
      }
    }

    .login {
      padding: 20px;
      font-size: 20px;
      font-weight: 500;
      text-align: center;

      .title {
        text-align: center;
        margin: 15px;
      }

      .el-input {
        margin: 5px 0;
      }

      .el-button {
        margin: 15px 0;
      }
    }
  }
}
</style>