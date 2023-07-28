package top.xclhove.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.xclhove.spring.common.Result;
import top.xclhove.spring.entity.DTO.Admin;
import top.xclhove.spring.service.impl.SystemConfigService;
import top.xclhove.spring.service.impl.SystemStatusService;

@RestController
@RequestMapping("/system")
public class SystemController {
    @Autowired
    private SystemStatusService systemStatusService;
    @Autowired
    private SystemConfigService systemConfigService;

    @PostMapping("/init")
    public Result systemInit(@RequestBody Admin admin) {
        return systemConfigService.systemInit(admin);
    }

    @PostMapping("/backLogin")
    public Result backLogin (@RequestBody Admin admin) {
        return systemConfigService.backLogin(admin);
    }

    @GetMapping("/statuses")
    public Result getSystemStatus() {
        return systemStatusService.getSystemStatus();
    }
}