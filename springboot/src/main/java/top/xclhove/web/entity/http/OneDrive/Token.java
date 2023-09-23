package top.xclhove.web.entity.http.OneDrive;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 接收微软OneDrive服务器返回有关兑换token数据
 *
 * @author xclhove
 */
@NoArgsConstructor
@Data
public class Token {
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("scope")
    private String scope;
    @JsonProperty("expires_in")
    private Integer expiresIn;
    @JsonProperty("ext_expires_in")
    private Integer extExpiresIn;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("error")
    private String error;
    @JsonProperty("error_description")
    private String errorDescription;
    @JsonProperty("error_codes")
    private List<Integer> errorCodes;
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("trace_id")
    private String traceId;
    @JsonProperty("correlation_id")
    private String correlationId;
    @JsonProperty("error_uri")
    private String errorUri;
}
