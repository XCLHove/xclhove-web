package top.xclhove.spring.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.xclhove.spring.common.Constant.Constant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    //状态码
    private String code;
    private String message;
    private String type;
    private Object data;

    //请求成功
    public static Result success() {
        return new Result(Constant.CODE_200, Constant.SUCCESS, Constant.SUCCESS, null);
    }

    public static Result success(Object data) {
        return new Result(Constant.CODE_200, Constant.SUCCESS, Constant.SUCCESS, data);
    }

    public static Result success(String message) {
        return new Result(Constant.CODE_200, message, Constant.SUCCESS, null);
    }

    public static Result success(String message, Object data) {
        return new Result(Constant.CODE_200, message, Constant.SUCCESS, data);
    }

    public static Result success(String code, String message, Object data) {
        return new Result(code, message, Constant.SUCCESS, data);
    }

    //请求失败
    public static Result error() {
        return new Result(Constant.CODE_500, Constant.ERROR, Constant.ERROR, null);
    }

    public static Result error(String message) {
        return new Result(Constant.CODE_500, message, Constant.ERROR, null);
    }

    public static Result error(String message, Object data) {
        return new Result(Constant.CODE_500, message, Constant.ERROR, null);
    }

    public static Result error(String code, String message) {
        return new Result(code, message, Constant.ERROR, null);
    }

    public static Result error(String code, String message, Object data) {
        return new Result(code, message, Constant.ERROR, data);
    }
}
