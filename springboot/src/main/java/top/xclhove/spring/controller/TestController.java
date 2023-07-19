package top.xclhove.spring.controller;

import org.springframework.web.bind.annotation.*;
import top.xclhove.spring.common.Constant;

import static top.xclhove.spring.utils.interpolation.Interpolations.indexed;

@RestController
@RequestMapping("/test")
public class TestController {
    //增
    @PostMapping
    public String add(@RequestBody Object object) {
        return indexed("{0} add {1}", Constant.TEST_MESSAGE, object);
    }

    //删
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        return indexed("{0} delete id={1}", Constant.TEST_MESSAGE, id);
    }

    //改
    @PutMapping
    public String update(@RequestBody Object object) {
        return indexed("{0} update {1}", Constant.TEST_MESSAGE, object);
    }

    //查
    @GetMapping("/{id}")
    public String get(@PathVariable Integer id, @RequestParam String message) {
        return indexed("{0} get id={1}, message={2}", Constant.TEST_MESSAGE, id, message);
    }
}
