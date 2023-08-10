package top.xclhove.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.xclhove.web.common.Result;
import top.xclhove.web.service.impl.LinkServiceImpl;

@RestController
@RequestMapping("/links")
public class LinkController {

    @Autowired
    private LinkServiceImpl linkServiceImpl;

    @GetMapping
    public Result search(@RequestParam String searchText,
                         @RequestParam Integer pageNumber,
                         @RequestParam Integer pageSize
    ) {
        return linkServiceImpl.search(searchText, pageNumber, pageSize);
    }
}
