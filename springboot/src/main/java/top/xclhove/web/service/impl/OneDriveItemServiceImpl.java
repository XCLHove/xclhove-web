package top.xclhove.web.service.impl;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.xclhove.web.common.Constant.OneDriveKey;
import top.xclhove.web.common.Result;
import top.xclhove.web.entity.OneDrive;
import top.xclhove.web.entity.OneDriveInfo.OneDriveInfo;
import top.xclhove.web.entity.OneDriveItem;
import top.xclhove.web.entity.http.httpOneDrive.HttpOneDriveItem;
import top.xclhove.web.entity.http.httpOneDrive.ParentReference;
import top.xclhove.web.http.OneDriveHttpResponse;
import top.xclhove.web.mapper.OneDriveItemMapper;
import top.xclhove.web.utils.interpolation.Interpolations;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OneDriveItemServiceImpl extends ServiceImpl<OneDriveItemMapper, OneDriveItem> {
    @Autowired
    private OneDriveServiceImpl oneDriveService;

    /**
     * 获取根目录信息并存储到MySQL
     *
     * @param oneDrive
     * @return OneDriveRootInfo
     */
    public OneDriveInfo getOneDriveRootInfo(OneDrive oneDrive) {
        //通过access_token获取根目录信息
        HttpResponse httpResponse = OneDriveHttpResponse.getQueryRootInfoResponse(oneDrive.getAccess_token());

        //转换以便提取
        HttpOneDriveItem responseBody = HttpOneDriveItem.parse(httpResponse.body());
        ParentReference parentReference = responseBody.getParentReference();

        //提取根目录driveId
        String driveId = parentReference.getDriveId();

        //更新driveId
        oneDrive.setDriveId(driveId);

        //存储
        oneDriveService.updateById(oneDrive);

        //提取根目录信息
        String name = oneDrive.getName();
        String itemId = responseBody.getId();
        String size = responseBody.getSize();
        int isRoot = 1;

        //获取数据库对应根目录信息
        LambdaQueryWrapper<OneDriveItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OneDriveItem::getItemId, itemId);
        OneDriveItem oneDriveItem = this.getOne(queryWrapper);
        oneDriveItem = (oneDriveItem != null) ? oneDriveItem : new OneDriveItem();

        //更新
        oneDriveItem.setName(name);
        oneDriveItem.setItemId(itemId);
        oneDriveItem.setSize(size);
        oneDriveItem.setIsRoot(isRoot);
        oneDriveItem.setOneDriveId(oneDrive.getId());

        //储存
        this.saveOrUpdate(oneDriveItem);

        //返回结果处理
        OneDriveInfo oneDriveInfo = new OneDriveInfo();
        oneDriveInfo.setOneDrive(oneDrive);
        oneDriveInfo.setOneDriveItem(oneDriveItem);

        return oneDriveInfo;
    }

    /**
     * 从指定存储源(drive)的指定目录(item)开始递归获取所有子目录与文件信息并存储到MySQL
     *
     * @param oneDrive
     * @param itemId
     */
    public void getItemChildrenInfo(OneDrive oneDrive, String itemId) {
        //获取目录的子项信息
        HttpResponse httpResponse = OneDriveHttpResponse.getQueryItemChildrenInfoResponse(
                oneDrive.getDriveId(),
                itemId,
                oneDrive.getAccess_token()
        );
        String itemChildrenInfo = httpResponse
                .body().
                replace("@microsoft.graph.downloadUrl", "downloadUrl");

        //将子项信息转换为JSON数组
        JSONArray jsonArray = JSONObject.parseObject(itemChildrenInfo).getJSONArray("value");

        //遍历(JSON数组)目录的子项
        for (Object jsonArrayItem : jsonArray) {
            //子项信息转换为java对象
            HttpOneDriveItem httpOneDriveItem = HttpOneDriveItem.parse(jsonArrayItem.toString());

            //提取子项信息
            String name = httpOneDriveItem.getName();
            String childItemId = httpOneDriveItem.getId();
            String size = httpOneDriveItem.getSize();
            Integer isFile = httpOneDriveItem.getIsFile();
            String downloadUrl = httpOneDriveItem.getDownloadUrl();
            Integer parentId = httpOneDriveItem.getPatentId();
            Integer isRoot = httpOneDriveItem.getIsRoot();

            //处理子项信息
            LambdaQueryWrapper<OneDriveItem> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(OneDriveItem::getItemId, httpOneDriveItem.getParentReference().getId());
            OneDriveItem parentItem = this.getOne(queryWrapper);
            try {
                //处理parentId(top.xclhove.web.entity.http.httpOneDrive.HttpOneDriveItem-->parentId默认为0)
                parentId = parentItem.getId();
            } catch (NullPointerException nullPointerException) {
                //nullPointerException不做处理
            } catch (Exception exception) {
                //其它异常，记录到日志
                log.error(exception.toString());
            }
            //处理isRoot
            isRoot = (parentId > 0) ? OneDriveKey.NOT_IS_ROOT : OneDriveKey.IS_ROOT;

            //获取数据库对应子项信息
            LambdaQueryWrapper<OneDriveItem> DBQueryWrapper = new LambdaQueryWrapper<>();
            DBQueryWrapper.eq(OneDriveItem::getItemId, childItemId);
            OneDriveItem DBOneDriveItem = this.getOne(DBQueryWrapper);
            DBOneDriveItem = (DBOneDriveItem == null) ? new OneDriveItem() : DBOneDriveItem;

            //更新子项信息
            DBOneDriveItem.setName(name);
            DBOneDriveItem.setItemId(childItemId);
            DBOneDriveItem.setSize(size);
            DBOneDriveItem.setIsFile(isFile);
            DBOneDriveItem.setDownloadUrl(downloadUrl);
            DBOneDriveItem.setParentId(parentId);
            DBOneDriveItem.setIsRoot(isRoot);
            DBOneDriveItem.setOneDriveId(oneDrive.getId());

            //储存
            this.saveOrUpdate(DBOneDriveItem);

            //判断该子项是否有子项，有则继续获取并储存
            Integer childCount = 0;
            try {
                childCount = httpOneDriveItem.getFolder().getChildCount();
            } catch (NullPointerException nullPointerException) {
                //nullPointerException不做处理
            } catch (Exception exception) {
                //其它异常，记录到日志
                log.error(exception.toString());
            }
            if (childCount > 0) {
                getItemChildrenInfo(oneDrive, childItemId);
            }
        }
    }

    /**
     * 获取所有drive的所有目录和文件信息
     *
     * @return Result
     */
    public Result getAllItemInfo() {
        //log
        log.info("开始获取所有存储源的所有目录和文件信息……");

        //获取所有存储源
        List<OneDrive> oneDrives = oneDriveService.list();
        if (oneDrives.size() == 0) {
            return Result.success("无存储源！");
        }
        for (OneDrive oneDrive : oneDrives) { //遍历所有存储源
            //获取存储源根目录信息
            OneDriveInfo oneDriveInfo = getOneDriveRootInfo(oneDrive);

            //从存储源根目录开始递归获取所有子目录与文件信息
            getItemChildrenInfo(oneDriveInfo.getOneDrive(), oneDriveInfo.getOneDriveItem().getItemId());
        }

        String message = "获取所有存储源的所有目录和文件信息完成！";
        log.info(message);
        return Result.success(message);
    }

    /**
     * 获取指定目录下的文件，默认为根目录
     * @param itemId
     * @return oneDriveItems
     */
    public Result getOneDriveItems(Integer itemId) {
        Map<String, Object> data = new HashMap<>();
        LambdaQueryWrapper<OneDriveItem> queryWrapper = new LambdaQueryWrapper<>();
        if (itemId == 0) {  //获取根目录
            queryWrapper.eq(OneDriveItem::getIsRoot, 1);
        } else {    //获取指定目录
            queryWrapper.eq(OneDriveItem::getParentId, itemId);
        }
        List<OneDriveItem> oneDriveItems = this.list(queryWrapper);
        data.put("oneDriveItems", oneDriveItems);
        return Result.success(data);
    }

    public Result search(String searchText) {
        Map<String, Object> data = new HashMap<>();
        LambdaQueryWrapper<OneDriveItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(OneDriveItem::getName, searchText);
        List<OneDriveItem> oneDriveItems = this.list(queryWrapper);
        data.put("oneDriveItems", oneDriveItems);
        return Result.success(data);
    }

    public void download(HttpServletResponse response, String itemId) {
        //通过itemId获取下载链接
        LambdaQueryWrapper<OneDriveItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OneDriveItem::getItemId, itemId);
        OneDriveItem oneDriveItem = this.getOne(queryWrapper);
        String downloadUrl = oneDriveItem.getDownloadUrl();

        //302重定向到下载链接
        response.setStatus(302);
        response.setHeader("Location", downloadUrl);
    }
}
