package top.xclhove.web.entity.http.OneDrive;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.text.DecimalFormat;

@Data
public class Item {
    private String createdDateTime;
    private String id;
    private String lastModifiedDateTime;
    private String name;
    private Integer isFile = 0;
    private Integer isRoot = 0;
    private Integer patentId;
    @JsonProperty("@microsoft.graph.downloadUrl")
    private String downloadUrl;
    private String size;
    private ParentReference parentReference;
    private Folder folder;
    private Error error;

    public String getSize() {
        if ("0".equals(this.size)) {
            return this.size + "B";
        }
        int sizeLevel = 1;
        long longSize = Long.parseLong(this.size);
        long remainder = 0L;
        while (longSize > 1024) {
            remainder = longSize % 1024;
            longSize /= 1024;
            sizeLevel++;
        }

        String sizeType = "";
        switch (sizeLevel) {
            case 1:
                sizeType = "B";
                break;
            case 2:
                sizeType = "KB";
                break;
            case 3:
                sizeType = "MB";
                break;
            case 4:
                sizeType = "GB";
                break;
            case 5:
                sizeType = "TB";
                break;
            case 6:
                sizeType = "PB";
                break;
        }
        float floatSize = longSize + remainder / 1000f;
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        return decimalFormat.format(floatSize) + sizeType;
    }

    public Integer getIsFile() {
        if (this.downloadUrl != null &&
                !this.downloadUrl.isEmpty() &&
                this.downloadUrl.startsWith("http")
        ) {
            this.isFile = 1;
        }
        return isFile;
    }
}
