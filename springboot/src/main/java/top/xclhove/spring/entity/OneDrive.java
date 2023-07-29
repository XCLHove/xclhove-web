package top.xclhove.spring.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("OneDrive")
public class OneDrive {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private String client_id;
    private String client_secret;
    private String redirect_uri;
    private String access_token;
    private String refresh_token;
}
