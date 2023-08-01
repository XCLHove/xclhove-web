package top.xclhove.spring.common.Constant;

public interface OneDriveKey {
    String CLIENT_ID = "client_id";
    String REDIRECT_URI = "redirect_uri";
    String CLIENT_SECRET = "client_secret";
    String REFRESH_TOKEN = "refresh_token";
    String GRANT_TYPE = "grant_type";
    String ACCESS_TOKEN = "access_token";
    String BEARER = "Bearer";
    Integer IS_ROOT = 1;
    Integer NOT_IS_ROOT = 0;
    public interface Api {
        String TOKEN = "https://login.microsoftonline.com/common/oauth2/v2.0/token";
        String DRIVE_ITEM = "https://graph.microsoft.com/v1.0";
    }
    public interface Path {
        String ROOT = "/me/drive/root";
        String CHILDREN = "/children";
    }
    public interface HttpError {
        String INVALID_AUTHENTICATION_TOKEN = "InvalidAuthenticationToken";
        String TOKEN_IS_EXPIRED = ".*(the token is expired)+.*";
    }
}
