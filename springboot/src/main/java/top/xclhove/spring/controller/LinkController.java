package top.xclhove.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.xclhove.spring.common.Result;
import top.xclhove.spring.service.LinkService;

@RestController
@RequestMapping("/links")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @GetMapping
    public Result search(@RequestParam String searchText,
                         @RequestParam Integer pageNumber,
                         @RequestParam Integer pageSize
    ) {
        return linkService.search(searchText, pageNumber, pageSize);
    }
}
