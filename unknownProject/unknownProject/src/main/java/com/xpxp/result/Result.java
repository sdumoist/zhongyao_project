package com.xpxp.result;

import com.xpxp.Constant.ErrorConstant;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer code;  // 编码：200 表示成功，其它表示失败
    private String msg;
    private T data;

    // 成功：有数据返回
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setData(data);
        return result;
    }

    // 成功：无数据返回
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode(200);
        return result;
    }

    // 失败响应：使用枚举常量
    public static <T> Result<T> error(ErrorConstant errorConstant) {
        Result<T> result = new Result<>();
        result.setCode(errorConstant.getCode());
        result.setMsg(errorConstant.getMessage());
        return result;
    }

    // 失败响应：使用枚举常量 + 自定义消息
    public static <T> Result<T> error(ErrorConstant errorConstant, String customMessage) {
        Result<T> result = new Result<>();
        result.setCode(errorConstant.getCode());
        result.setMsg(customMessage);
        return result;
    }

    // 失败响应：仅传入错误信息，默认状态码为500
    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMsg(msg);
        return result;
    }

    // ✅ 新增：支持自定义错误码和错误信息
    public static <T> Result<T> error(int code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    // 示例：按照你类里已有字段/构造函数调整
    public static <T> Result<T> failure(String msg) {
        // 若你已有 error(msg)：
        // return error(msg);

        // 或者自己构造（根据你的 Result 字段来）：
        Result<T> r = new Result<>();
        r.setCode(500);
        r.setMsg(msg);
//        r.setSuccess(false);
        return r;
    }
}
