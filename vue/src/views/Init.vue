<script setup>
import {ref} from "vue";
import prompt from "@/utils/prompt";
import request from "@/utils/request";
import router from "@/router/router";

const initFormRef = ref()
const validate = (rule, value, callback) => {
  const pattern = /[^[a-z0-9A-Z.*_]+/g
  if (!pattern.test(value)) {
    return true
  }
  return callback(new Error('仅支持"英文字母"、"数字"、"."、"*"和"_"'))
}
const validatePassword2 = (rule, value, callback) => {
  if (value === admin.value.password) {
    return true
  }
  return callback(new Error('确认密码与密码不一致'))
}
const initFormRules = ref({
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
  password2: [
    {required: true, message: '请输入确认密码', trigger: 'blur'},
    {min: 5, max: 20, message: '确认密码长度为5-20位', trigger: 'blur'},
    {validator: validate, message: '仅支持英文字母与数字', trigger: 'blur'},
    {validator: validatePassword2, trigger: 'blur'},
  ],
})
const admin = ref({
  backAccount: '',
  password: '',
  password2: '',
})

const backAccountRef = ref()
const passwordRef = ref()
const password2Ref = ref()

const systemInit = () => {
  initFormRef.value.validate(valid => {
    if (valid) {
      request.post(
          "/system/init",
          admin.value
      ).then(result => {
        prompt(result.message, result.type)
        router.push('/backLogin')
      })
    }
  })
}

const systemCheck = () => {
  request.get(
      "/system/statuses"
  ).then(result => {
    const systemStatuses = result.data.systemStatuses
    const installedConfig = systemStatuses.filter(status => status.name === 'installed')[0]
    console.log(installedConfig)
    if (installedConfig.value === 1) {
      router.push('/backLogin')
    }
  })
}

systemCheck()
</script>

<template>
  <Load>
    <div class="initContainer">
      <Head>系统初始化</Head>
      <div class="backAccountInit">
        <div class="part logo">
          <img src="../image/logo/XCLHove-logo.png" alt="logo" title="XCLHove-logo" height="400" width="400"
               @click="router.push('/')">
        </div>
        <div class="part info">
          <img src="/favicon.ico" alt="XCLHove-icon" title="XCLHove-icon" width="30" height="30">
          <div class="title">初始化后台管理员账号</div>
          <el-form ref="initFormRef" :model="admin" :rules="initFormRules" :inline-message="true" status-icon>
            <el-form-item prop="backAccount">
              <el-input ref="backAccountRef" v-model.trim="admin.backAccount" placeholder="账号" prefix-icon="User"
                        autofocus
                        @keyup.enter="$refs.passwordRef.focus()"
                        @keyup.down="$refs.passwordRef.focus()"
                        @keyup.up="$refs.password2Ref.focus()"/>
            </el-form-item>
            <el-form-item prop="password">
              <el-input ref="passwordRef" v-model.trim="admin.password" placeholder="密码" show-password
                        prefix-icon="Lock"
                        @keyup.enter="$refs.password2Ref.focus()"
                        @keyup.down="$refs.password2Ref.focus()"
                        @keyup.up="$refs.backAccountRef.focus()"/>
            </el-form-item>
            <el-form-item prop="password2">
              <el-input ref="password2Ref" v-model.trim="admin.password2" placeholder="确认密码" show-password
                        prefix-icon="Lock"
                        @keyup.enter="systemInit()"
                        @keyup.down="$refs.backAccountRef.focus()"
                        @keyup.up="$refs.passwordRef.focus()"/>
            </el-form-item>
            <el-button type="primary" class="width100" @click="systemInit()">确定</el-button>
          </el-form>
        </div>
      </div>
    </div>
  </Load>
</template>

<style scoped lang="less">
@import "@/assets/main";

.initContainer {
  width: 100%;
  height: 100vh;

  .backAccountInit {
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

    .info {
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