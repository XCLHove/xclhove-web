import {ElMessage} from "element-plus";

const prompt = (message, type) => {
    ElMessage({
        showClose: true,
        duration: 1000,
        message: message,
        type: type
    })
}
export default prompt