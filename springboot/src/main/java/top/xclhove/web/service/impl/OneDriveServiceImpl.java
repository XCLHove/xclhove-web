package top.xclhove.web.service.impl;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.xclhove.web.common.Constant.OneDriveKey;
import top.xclhove.web.common.Result;
import top.xclhove.web.entity.DTO.Storage;
import top.xclhove.web.entity.OneDrive;
import top.xclhove.web.entity.http.OneDrive.Token;
import top.xclhove.web.http.OneDriveHttp;
import top.xclhove.web.mapper.OneDriveMapper;
import top.xclhove.web.utils.interpolation.Interpolations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OneDriveServiceImpl extends ServiceImpl<OneDriveMapper, OneDrive> {

    public Result refreshAllOneDriveAccessToken() {
        List<OneDrive> oneDrives = this.list();
        for (OneDrive oneDrive : oneDrives) {
            this.refreshOneDriveAccessToken(oneDrive);
        }
        return Result.success("刷新成功！");
    }

    public Result getAll() {
        Map<String, Object> data = new HashMap<>();
        List<OneDrive> oneDrives = this.list();
        data.put("oneDrives", oneDrives);
        return Result.success(data);
    }

    public Result refreshOneDriveAccessToken(OneDrive oneDrive) {
        boolean refreshSuccess = false;

        String params = Interpolations.indexed(
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
                .createPost(OneDriveKey.Api.TOKEN)
                .body(params, ContentType.FORM_URLENCODED.getValue())
                .execute();

        int responseStatus = response.getStatus();
        if (responseStatus != HttpStatus.OK.value()) {
            log.info(String.valueOf(response));
            return Result.error("请求失败！", oneDrive);
        }

        JSONObject jsonResponseBody = JSONObject.parseObject(response.body());
        String newAccessToken = jsonResponseBody.getString(OneDriveKey.ACCESS_TOKEN);
        String newRefreshToken = jsonResponseBody.getString(OneDriveKey.REFRESH_TOKEN);

        oneDrive.setAccess_token(newAccessToken);
        oneDrive.setRefresh_token(newRefreshToken);

        refreshSuccess = this.updateById(oneDrive);
        if (refreshSuccess) {
            log.info("刷新存储源：'{}'成功！", oneDrive.getName());
            return Result.success("更新成功！", oneDrive);
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

        return Result.error("请求成功，更新失败！", oneDrive);
    }

    public Result getToken(Storage storage) {
        Map<String, Object> postData = new HashMap<>();
        postData.put(OneDriveKey.CLIENT_ID, storage.getClientId());
        postData.put(OneDriveKey.CLIENT_SECRET, storage.getClientSecret());
        postData.put(OneDriveKey.REDIRECT_URI, storage.getRedirectUri());
        postData.put(OneDriveKey.CODE, storage.getCode());
        postData.put(OneDriveKey.GRANT_TYPE, OneDriveKey.GrantType.AUTHORIZATION_CODE);

        String postResult = HttpUtil.post(OneDriveHttp.getTokenApi(), postData);
        Token token = JSON.parseObject(postResult, Token.class);

        String errorDescription = token.getErrorDescription();
        if (errorDescription != null) {
            return Result.error(errorDescription.split("\\r\\n")[0].split(": ")[1]);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        return Result.success(data);
    }

    public Result addOrUpdate(OneDrive oneDrive) {
        boolean operationSuccess = false;
        try {
            operationSuccess = this.saveOrUpdate(oneDrive);
        } catch (Exception exception) {
            //operationSuccess = false;
        }
        if (!operationSuccess) {
            return Result.error("操作失败，请再次尝试！");
        }
        return Result.success("操作成功！");
    }
}
