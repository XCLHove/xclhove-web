import axios from 'axios'
import config from "@/utils/config.js";
import prompt from "@/utils/prompt";

//读取外部配置文件后端地址
const serverUrl = config.serverUrl ? config.serverUrl : 'http://localhost:8080'

const request = axios.create({
    baseURL: serverUrl,  // 这里加上后端接口前缀，后端必须进行跨域配置
    timeout: 10000 // ms
})

// request 拦截器
// 可以自请求发送前对请求做一些处理
// 比如统一加token，对请求参数统一加密
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    // 设置请求头
    // config.headers['token'] = 'token';
    return config;
}, error => {
    return Promise.reject(error);
});

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data;
        // 如果是返回的文件
        if (response.config.responseType === 'blob') {
            return res;
        }
        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res;
        }
        return res;
    },
    error => {
        //console.log('err:' + error); // for debug
        prompt('请求数据失败', 'error')
        return Promise.reject(error);
    }
)
export default request

