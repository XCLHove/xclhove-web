import {ElMessage} from "element-plus";

const prompt = (message, type = 'info', durationSecond= 1) => {
    ElMessage({
        showClose: true,
        duration: durationSecond * 1000,
        message: message,
        type: type
    })
}
export default prompt