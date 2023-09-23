package top.xclhove.web.http;

import top.xclhove.web.common.Constant.OneDriveKey;

import static top.xclhove.web.utils.interpolation.Interpolations.indexed;

public class OneDriveHttp {
    /**
     * 获取OneDrive http通用请求头
     *
     * @param accessToken
     * @return commonApiHeader
     */
    public static String getCommonApiHeader(String accessToken) {
        return indexed(
                "{0} {1}",
                OneDriveKey.Header.BEARER, accessToken
        );
    }

    /**
     * 获取查询根目录信息的api
     *
     * @return queryRootInfoApi
     */
    public static String getQueryRootInfoApi() {
        return indexed(
                "{0}{1}",
                OneDriveKey.Api.DRIVE_ITEM, OneDriveKey.Path.ROOT
        );
    }

    /**
     * 获取查询指定目录子项信息的api
     *
     * @param driveId
     * @param itemId
     * @return queryItemChildrenInfoApi
     */
    public static String getQueryItemChildrenInfoApi(String driveId, String itemId) {
        return indexed(
                "{0}/drives/{1}/items/{2}/children",
                OneDriveKey.Api.DRIVE_ITEM, driveId, itemId
        );
    }

    /**
     * 获取下载链接的api
     *
     * @param driveId driveId
     * @param itemId  itemId
     * @return downloadUrlApi
     */
    public static String getDownloadUrlApi(String driveId, String itemId) {
        return indexed(
                "{0}/drives/{1}/items/{2}/content",
                OneDriveKey.Api.DRIVE_ITEM, driveId, itemId
        );
    }

    /**
     * 获取兑换代码获取访问令牌的api
     *
     * @return 兑换代码获取访问令牌的api
     */
    public static String getTokenApi() {
        return OneDriveKey.Api.TOKEN;
    }
}
