package top.xclhove.spring.service.impl;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.xclhove.spring.common.Constant.ContentType;
import top.xclhove.spring.common.Constant.OneDriveKey;
import top.xclhove.spring.common.Result;
import top.xclhove.spring.entity.OneDrive;
import top.xclhove.spring.mapper.OneDriveMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static top.xclhove.spring.utils.interpolation.Interpolations.indexed;

@Service
@Slf4j
public class OneDriveServiceImpl extends ServiceImpl<OneDriveMapper, OneDrive> {
    public Result getAll() {
        Map<String, Object> data = new HashMap<>();
        List<OneDrive> oneDrives = this.list();
        data.put("oneDrives", oneDrives);
        return Result.success(data);
    }
    public Result refreshStorageSource(OneDrive oneDrive) {
        boolean refreshSuccess = false;

        String params = indexed(
                "{0}={1}" +
                        "&{2}={3}" +
                        "&{4}={5}" +
                        "&{6}={7}" +
                        "&{8}={9}",
                OneDriveKey.CLIENT_ID, oneDrive.getClient_id(),
                OneDriveKey.REDIRECT_URI, oneDrive.getRedirect_uri(),
                OneDriveKey.CLIENT_SECRET, oneDrive.getClient_secret(),
                OneDriveKey.REFRESH_TOKEN, oneDrive.getRefresh_token(),
                OneDriveKey.GRANT_TYPE, OneDriveKey.REFRESH_TOKEN);
        HttpResponse response = HttpUtil
                .createPost(OneDriveKey.API_URL)
                .body(params, ContentType.X_WWW_FORM_URLENCODED)
                .execute();

        int responseStatus = response.getStatus();
        if (responseStatus != HttpStatus.OK.value()) {
            log.info(String.valueOf(response));
            return Result.error("请求失败！", refreshSuccess);
        }

        JSONObject jsonResponseBody = JSONObject.parseObject(response.body());
        String newAccessToken = jsonResponseBody.getString(OneDriveKey.ACCESS_TOKEN);
        String newRefreshToken = jsonResponseBody.getString(OneDriveKey.REFRESH_TOKEN);

        oneDrive.setAccess_token(newAccessToken);
        oneDrive.setRefresh_token(newRefreshToken);

        refreshSuccess = this.updateById(oneDrive);
        if (refreshSuccess) {
            log.info("刷新存储源：'{}'成功！", oneDrive.getName());
            return Result.success("更新成功！", refreshSuccess);
        }

        log.info(
                "刷新存储源：'{}'失败！\n" +
                        "newAccessToken:\n" +
                        "---------------\n" +
                        "{}\n" +
                        "---------------\n" +
                        "newRefreshToken:\n" +
                        "---------------\n" +
                        "{}\n" +
                        "---------------\n",
                oneDrive.getName(),
                newAccessToken,
                newRefreshToken);

        return Result.error("请求成功，更新失败！", refreshSuccess);
    }
}
