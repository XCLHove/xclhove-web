package top.xclhove.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.xclhove.web.common.Result;
import top.xclhove.web.service.impl.OneDriveItemServiceImpl;

import javax.servlet.http.HttpServletResponse;

/**
 * @author xclhove
 */
@RestController
@RequestMapping("/oneDriveItems")
public class OneDriveItemController {
    @Autowired
    private OneDriveItemServiceImpl oneDriveItemService;

    @GetMapping
    public Result getOneDriveItems(@RequestParam(value = "itemId", required = false, defaultValue = "0") Integer itemId) {
        return oneDriveItemService.getOneDriveItems(itemId);
    }

    @GetMapping("/search/{searchText}")
    public Result search(@PathVariable String searchText) {
        return oneDriveItemService.search(searchText);
    }

    /**
     * 下载文件
     *
     * @param itemId itemId
     */
    @GetMapping("/download/{itemId}")
    public void download(HttpServletResponse response, @PathVariable String itemId) {
        oneDriveItemService.download(response, itemId);
    }

}
