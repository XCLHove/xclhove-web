import axios from "axios";

const config = await axios.get('/config.json').then(result => {
    return result.data
})
//导出以便引用
export default config