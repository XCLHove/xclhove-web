import axios from "axios";

let config = {
    serverUrl: 'http://localhost:8081',
    beiAnHao: '蜀ICP备2023003714号',
}
//读取配置
axios.get('/config.json').then(result => {
    config = result.data ? result.data : config
})
//导出以便引用
export default config