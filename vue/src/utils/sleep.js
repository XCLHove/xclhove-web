const sleep = (second) => {
    return new Promise(resolve => {
        setTimeout(() => {
            resolve()
        }, (second * 1000))
    })
}
export default sleep