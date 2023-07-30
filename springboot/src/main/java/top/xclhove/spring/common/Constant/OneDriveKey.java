package top.xclhove.spring.common.Constant;

public interface OneDriveKey {
    String CLIENT_ID = "client_id";
    String REDIRECT_URI = "redirect_uri";
    String CLIENT_SECRET = "client_secret";
    String REFRESH_TOKEN = "refresh_token";
    String GRANT_TYPE = "grant_type";
    String ACCESS_TOKEN = "access_token";
    String BEARER = "Bearer";
    public interface Api {
        String TOKEN = "https://login.microsoftonline.com/common/oauth2/v2.0/token";
        String FILE = "https://graph.microsoft.com/v1.0";
    }
    public interface Path {
        String root = "/me/drive/root/children";
    }
}
