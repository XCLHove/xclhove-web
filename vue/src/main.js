import '@/assets/main.css'
import '@/icon/alibaba/iconfont.css'
import {createApp} from 'vue'
import App from '@/App.vue'
//router
import router from "@/router/router";
//element-plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
//element-plus国际化
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
//axios封装
import request from "@/utils/request.js";
//消息提示
import  "@/utils/prompt.js"

const app = createApp(App)
app.use(router)
app.use(ElementPlus, {locale: zhCn})
app.config.globalProperties.request = request
app.mount('#app')
