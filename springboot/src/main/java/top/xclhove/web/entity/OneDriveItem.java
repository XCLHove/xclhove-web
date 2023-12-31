package top.xclhove.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
@TableName("OneDriveItem")
public class OneDriveItem {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private String itemId;
    private String size;
    private Integer isFile;
    private String downloadUrl;
    private Integer parentId;
    private Integer isRoot;
    private Integer oneDriveId;
}