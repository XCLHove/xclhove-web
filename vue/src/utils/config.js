import axios from "axios";

//读取配置
async function readConfig() {
    let config = {}
    await axios.get('/config.json').then(result => {
        config = result.data ? result.data : config
    })
    return config
}

const config = await readConfig()
//导出以便引用
export default config