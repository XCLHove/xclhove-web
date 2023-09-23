import '@/assets/main.less'
import '@/icon/alibaba/iconfont.css'
import {createApp} from 'vue'
import App from '@/App.vue'
import Head from '@/components/WebHead.vue'
import Load from '@/components/Load.vue'
//router
import router from "@/router/router";
//element-plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElIcons from '@element-plus/icons'
//element-plus国际化
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
//axios封装
import request from "@/utils/request.js";
//pinia
import pinia from "@/pinia/pinia";

const app = createApp(App)

app.use(router)
app.use(ElementPlus, {locale: zhCn})
app.use(pinia)

app.config.globalProperties.request = request

for (name in ElIcons) {
    app.component(name, ElIcons[name])
}
app.component('Load', Load)
app.component('Head', Head)

app.mount('#app')
