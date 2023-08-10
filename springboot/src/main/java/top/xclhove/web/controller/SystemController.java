package top.xclhove.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.xclhove.web.common.Result;
import top.xclhove.web.entity.DTO.Admin;
import top.xclhove.web.service.impl.SystemConfigServiceImpl;
import top.xclhove.web.service.impl.SystemStatusServiceImpl;

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