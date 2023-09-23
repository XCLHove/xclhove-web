package top.xclhove.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("SystemConfig")
public class SystemConfig {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private String value;
}
