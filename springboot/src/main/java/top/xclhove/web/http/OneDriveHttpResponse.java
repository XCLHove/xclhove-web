package top.xclhove.web.http;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;

import java.util.HashMap;
import java.util.Map;

public class OneDriveHttpResponse {
    /**
     * 获取查询存储源根目录信息的http请求
     *
     * @param accessToken access_token
     * @return HttpResponse
     */
    public static HttpResponse getQueryRootInfoResponse(String accessToken) {
        return HttpUtil
                .createGet(OneDriveHttp.getQueryRootInfoApi())
                .header(Header.AUTHORIZATION.getValue(), OneDriveHttp.getCommonApiHeader(accessToken))
                .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                .execute();
    }

    /**
     * 获取查询指定目录子项的http请求
     *
     * @param driveId     driveId
     * @param itemId      itemId
     * @param accessToken access_token
     * @return HttpResponse
     */
    public static HttpResponse getQueryItemChildrenInfoResponse(String driveId, String itemId, String accessToken) {
        return HttpUtil
                .createGet(OneDriveHttp.getQueryItemChildrenInfoApi(driveId, itemId))
                .auth(OneDriveHttp.getCommonApiHeader(accessToken))
//                .header(Header.AUTHORIZATION.getValue(), OneDriveHttp.getCommonApiHeader(accessToken))
                .contentType(ContentType.JSON.getValue())
//                .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                .execute();
    }
}
