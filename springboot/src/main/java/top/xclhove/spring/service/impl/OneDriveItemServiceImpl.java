package top.xclhove.spring.service.impl;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.xclhove.spring.common.Constant.OneDriveKey;
import top.xclhove.spring.entity.OneDrive;
import top.xclhove.spring.entity.OneDriveItem;
import top.xclhove.spring.entity.http.httpOneDrive.HttpOneDriveItem;
import top.xclhove.spring.entity.http.httpOneDrive.ParentReference;
import top.xclhove.spring.mapper.OneDriveItemMapper;

import java.util.List;

import static top.xclhove.spring.utils.interpolation.Interpolations.indexed;

@Service
public class OneDriveItemServiceImpl extends ServiceImpl<OneDriveItemMapper, OneDriveItem> {
    @Autowired
    private OneDriveServiceImpl oneDriveService;

    /**
     * 获取根目录信息
     *
     * @param oneDrive
     * @return OneDriveRootInfo
     */
    public OneDriveItem getOneDriveRootInfo(OneDrive oneDrive) {
        //通过access_token获取根目录信息
        HttpResponse httpResponse = HttpUtil
                .createGet(OneDriveKey.Api.DRIVE_ITEM +
                        OneDriveKey.Path.ROOT
                )
                .header(Header.AUTHORIZATION.getValue(), indexed("{0} {1}",
                        OneDriveKey.BEARER, oneDrive.getAccess_token())
                )
                .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                .execute();

        //转换
        HttpOneDriveItem responseBody = HttpOneDriveItem.parse(httpResponse.body());
        ParentReference parentReference = responseBody.getParentReference();

        //提取并存储获取到的driveId
        String driveId = parentReference.getDriveId();
        oneDrive.setDriveId(driveId);
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

        //储存
        this.saveOrUpdate(oneDriveItem);

        return oneDriveItem;
    }

    /**
     * 获取指定目录的子项
     *
     * @param oneDrive
     * @param itemId
     * @return itemChildren
     */
    public String getOneDriveItemChildren(OneDrive oneDrive, String itemId) {
        HttpResponse httpResponse = HttpUtil
                .createGet(OneDriveKey.Api.DRIVE_ITEM +
                        indexed("/me/drive/items/{0}/children", itemId)
                )
                .header(Header.AUTHORIZATION.getValue(),
                        indexed("{0} {1}",
                                OneDriveKey.BEARER, oneDrive.getAccess_token()
                        )
                )
                .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                .execute();
        return httpResponse.body().replace("@microsoft.graph.downloadUrl", "downloadUrl");
    }

    /**
     * 递归获取所有子项
     *
     * @param oneDrive
     * @param parentItemId
     */
    public void getChildrenItem(OneDrive oneDrive, String parentItemId) {
        //获取目录信息
        String itemChildChildren = getOneDriveItemChildren(oneDrive, parentItemId);

        //转换为JSON数组
        JSONArray jsonArray = JSONObject.parseObject(itemChildChildren).getJSONArray("value");

        //遍历目录的子项
        for (Object jsonArrayItem : jsonArray) {
            //转换
            HttpOneDriveItem httpOneDriveItem = HttpOneDriveItem.parse(jsonArrayItem.toString());

            //提取子项信息
            String name = httpOneDriveItem.getName();
            String itemId = httpOneDriveItem.getId();
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
                parentId = parentItem.getId();
            } catch (Exception exception) {
                //
            }
            isRoot = (parentId > 0) ? OneDriveKey.NOT_IS_ROOT : OneDriveKey.IS_ROOT;

            //获取数据库对应子项信息
            LambdaQueryWrapper<OneDriveItem> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(OneDriveItem::getItemId, itemId);
            OneDriveItem DBOneDriveItem = this.getOne(queryWrapper1);
            DBOneDriveItem = (DBOneDriveItem == null) ? new OneDriveItem() : DBOneDriveItem;

            //更新
            DBOneDriveItem.setName(name);
            DBOneDriveItem.setItemId(itemId);
            DBOneDriveItem.setSize(size);
            DBOneDriveItem.setIsFile(isFile);
            DBOneDriveItem.setDownloadUrl(downloadUrl);
            DBOneDriveItem.setParentId(parentId);
            DBOneDriveItem.setIsRoot(isRoot);

            //储存
            this.saveOrUpdate(DBOneDriveItem);

            //判断该子项是否有子项，有则继续获取并储存
            Integer childCount = 0;
            try {
                childCount = httpOneDriveItem.getFolder().getChildCount();
            } catch (Exception exception) {
                //
            }
            if (childCount > 0) {
                getChildrenItem(oneDrive, httpOneDriveItem.getId());
            }
        }
    }

    /**
     * 获取所有目录和文件的信息
     */
    public void getAllItemInfo() {
        List<OneDrive> oneDrives = oneDriveService.list();
        if (oneDrives.size() == 0) {
            return;
        }
        for (OneDrive oneDrive : oneDrives) { //遍历所有储存源
            //获取根目录信息
            OneDriveItem rootOneDriveItem = getOneDriveRootInfo(oneDrive);

            //获取根目录的子项
            HttpResponse httpResponse = HttpUtil
                    .createGet(OneDriveKey.Api.DRIVE_ITEM +
                            OneDriveKey.Path.ROOT +
                            OneDriveKey.Path.CHILDREN
                    )
                    .header(Header.AUTHORIZATION.getValue(),
                            indexed("{0} {1}",
                                    OneDriveKey.BEARER, oneDrive.getAccess_token()
                            )
                    )
                    .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                    .execute();
            String rootChildren = httpResponse.body().replace("@microsoft.graph.downloadUrl", "downloadUrl");

            //转换为JSON数组
            JSONArray jsonArray = JSONObject.parseObject(rootChildren).getJSONArray("value");

            //遍历根目录的子项
            for (Object jsonArrayItem : jsonArray) {
                //转换
                HttpOneDriveItem httpOneDriveItem = HttpOneDriveItem.parse(jsonArrayItem.toString());

                //提取子项信息
                String name = httpOneDriveItem.getName();
                String itemId = httpOneDriveItem.getId();
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
                    parentId = parentItem.getId();
                } catch (Exception exception) {
                    //
                }
                isRoot = (parentId > 0) ? OneDriveKey.NOT_IS_ROOT : OneDriveKey.IS_ROOT;

                //获取数据库对应子项信息
                LambdaQueryWrapper<OneDriveItem> queryWrapper1 = new LambdaQueryWrapper<>();
                queryWrapper1.eq(OneDriveItem::getItemId, itemId);
                OneDriveItem DBOneDriveItem = this.getOne(queryWrapper1);
                DBOneDriveItem = (DBOneDriveItem == null) ? new OneDriveItem() : DBOneDriveItem;

                //更新
                DBOneDriveItem.setName(name);
                DBOneDriveItem.setItemId(itemId);
                DBOneDriveItem.setSize(size);
                DBOneDriveItem.setIsFile(isFile);
                DBOneDriveItem.setDownloadUrl(downloadUrl);
                DBOneDriveItem.setParentId(parentId);
                DBOneDriveItem.setIsRoot(isRoot);

                //储存
                this.saveOrUpdate(DBOneDriveItem);

                //判断该子项是否有子项，有则继续获取并储存
                Integer childCount = 0;
                try {
                    childCount = httpOneDriveItem.getFolder().getChildCount();
                } catch (Exception exception) {
                    //
                }
                if (childCount > 0) {
                    getChildrenItem(oneDrive, httpOneDriveItem.getId());
                }

            }
        }
    }
}
