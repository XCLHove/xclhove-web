package top.xclhove.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.xclhove.spring.common.Result;
import top.xclhove.spring.entity.DTO.Admin;
import top.xclhove.spring.service.impl.SystemConfigServiceImpl;
import top.xclhove.spring.service.impl.SystemStatusServiceImpl;

@RestController
@RequestMapping("/system")
public class SystemController {
    @Autowired
    private SystemStatusServiceImpl systemStatusServiceImpl;
    @Autowired
    private SystemConfigServiceImpl systemConfigServiceImpl;

    @PostMapping("/init")
    public Result systemInit(@RequestBody Admin admin) {
        return systemConfigServiceImpl.systemInit(admin);
    }

    @PostMapping("/backLogin")
    public Result backLogin (@RequestBody Admin admin) {
        return systemConfigServiceImpl.backLogin(admin);
    }

    @GetMapping("/statuses")
    public Result getSystemStatus() {
        return systemStatusServiceImpl.getSystemStatus();
    }
}