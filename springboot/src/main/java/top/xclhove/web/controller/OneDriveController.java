package top.xclhove.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.xclhove.web.common.Result;
import top.xclhove.web.entity.DTO.Storage;
import top.xclhove.web.entity.OneDrive;
import top.xclhove.web.service.impl.OneDriveServiceImpl;

/**
 * @author xclhove
 */
@RestController
@RequestMapping("/oneDrives")
public class OneDriveController {
    @Autowired
    private OneDriveServiceImpl oneDriveService;

    @PostMapping("/token")
    public Result callback(@RequestBody Storage storage) {
        return oneDriveService.getToken(storage);
    }

    @GetMapping
    public Result getAll() {
        return oneDriveService.getAll();
    }

    @PostMapping
    public Result addOrUpdate(@RequestBody OneDrive oneDrive) {
        return oneDriveService.addOrUpdate(oneDrive);
    }
}
