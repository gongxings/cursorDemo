package com.rental.dataAnalysis.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // 通用错误
    SYSTEM_ERROR(500, "系统繁忙，请稍后重试"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "没有操作权限"),
    
    // 用户相关错误
    USER_NOT_FOUND(1001, "用户不存在"),
    USERNAME_EXISTS(1002, "用户名已存在"),
    PASSWORD_ERROR(1003, "密码错误"),
    
    // 房源相关错误
    HOUSE_NOT_FOUND(2001, "房源不存在"),
    NO_PERMISSION_TO_MODIFY(2002, "无权限修改此房源"),
    NO_PERMISSION_TO_DELETE(2003, "无权限删除此房源");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BusinessException exception() {
        return new BusinessException(this.code, this.message);
    }

    public BusinessException exception(String message) {
        return new BusinessException(this.code, message);
    }
} 