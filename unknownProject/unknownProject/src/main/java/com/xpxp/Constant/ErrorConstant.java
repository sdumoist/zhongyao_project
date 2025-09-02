package com.xpxp.Constant;

import lombok.Getter;

@Getter
public enum ErrorConstant {
    // 通用错误
    INTERNAL_SERVER_ERROR(500, "系统内部错误"),
    INVALID_PARAM(400, "参数校验失败"),
    
    // 认证相关
    USERNAME_EXISTS(4001, "用户名已存在"),
    USER_NOT_FOUND(4002, "用户不存在"),
    PASSWORD_WRONG(4003, "密码错误"),
    LOGIN_FAILED(4004,"用户名或密码错误"),
    BLOG_NOT_EXISTS(4102,"文章不存在"),
    COMMENT_HASLIKED(5001,"您已经点赞过该评论"),
    CATEGORY_EXISTS(6001,"分类名已存在"),
    HAS_FOLLOWED(7001,"您已经关注了该用户"),
    HAS_UNFOLLOWED(7002,"您已经取关了该用户");


    // 业务相关
//    ORDER_NOT_PAID(5001, "订单未支付");

    // Getter
    private final int code;
    private final String message;

    ErrorConstant(int code, String message) {
        this.code = code;
        this.message = message;
    }

}