package top.xclhove.spring.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.xclhove.spring.common.Result;
import top.xclhove.spring.entity.Link;
import top.xclhove.spring.mapper.LinkMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> {
    @Autowired
    private LinkMapper linkMapper;

    public Result search(String searchText,
                         Integer pageNumber,
                         Integer pageSize) {
        Map<String, Object> data = new HashMap<>();
        pageNumber = (pageNumber - 1) * pageSize;
        List<Link> links = linkMapper.search(searchText, pageNumber, pageSize);
        data.put("links", links);
        int total = linkMapper.searchTotal(searchText);
        data.put("total", total);
        return Result.success(data);
    }
}
