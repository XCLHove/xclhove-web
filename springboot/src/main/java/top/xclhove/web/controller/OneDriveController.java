package top.xclhove.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.xclhove.web.common.Result;
import top.xclhove.web.service.impl.OneDriveServiceImpl;

@RestController
@RequestMapping("/oneDrives")
public class OneDriveController {
    @Autowired
    private OneDriveServiceImpl oneDriveService;

    @GetMapping
    public Result getAll() {
        return oneDriveService.getAll();
    }
}
