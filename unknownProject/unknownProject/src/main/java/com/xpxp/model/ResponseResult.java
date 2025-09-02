package com.xpxp.model;

import lombok.Data;

/**
 * 通用API响应包装类
 */
@Data
public class ResponseResult<T> {
    
    /**
     * 响应状态码
     */
    private Integer code;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 私有构造函数
     */
    private ResponseResult() {
    }
    
    /**
     * 成功响应（无数据）
     */
    public static <T> ResponseResult<T> success() {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(200);
        result.setMessage("操作成功");
        return result;
    }
    
    /**
     * 成功响应（有数据）
     */
    public static <T> ResponseResult<T> success(T data) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }
    
    /**
     * 成功响应（自定义消息）
     */
    public static <T> ResponseResult<T> success(String message, T data) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
    
    /**
     * 失败响应
     */
    public static <T> ResponseResult<T> error(String message) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(500);
        result.setMessage(message);
        return result;
    }
    
    /**
     * 失败响应（自定义状态码）
     */
    public static <T> ResponseResult<T> error(Integer code, String message) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
    
    /**
     * 失败响应（自定义状态码和数据）
     */
    public static <T> ResponseResult<T> error(Integer code, String message, T data) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
    
    /**
     * 参数错误响应
     */
    public static <T> ResponseResult<T> badRequest(String message) {
        return error(400, message);
    }
    
    /**
     * 未授权响应
     */
    public static <T> ResponseResult<T> unauthorized(String message) {
        return error(401, message);
    }
    
    /**
     * 禁止访问响应
     */
    public static <T> ResponseResult<T> forbidden(String message) {
        return error(403, message);
    }
    
    /**
     * 资源不存在响应
     */
    public static <T> ResponseResult<T> notFound(String message) {
        return error(404, message);
    }
    
    /**
     * 服务器内部错误响应
     */
    public static <T> ResponseResult<T> serverError(String message) {
        return error(500, message);
    }
    
    /**
     * 检查是否成功
     */
    public boolean isSuccess() {
        return code != null && code == 200;
    }
    
    /**
     * 检查是否失败
     */
    public boolean isError() {
        return !isSuccess();
    }
    
    /**
     * 获取数据（如果成功）
     */
    public T getDataOrThrow() {
        if (isSuccess()) {
            return data;
        } else {
            throw new RuntimeException("API调用失败: " + message);
        }
    }
} 
 
 
 
 
 
 