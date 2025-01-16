package com.rental.dataAnalysis.common;

import lombok.Getter;

@Getter
public enum ErrorCode {
    SUCCESS(200, "操作成功"),
    PARAMS_ERROR(400, "请求参数错误"),
    UNAUTHORIZED(401, "未登录或token已过期"),
    FORBIDDEN(403, "没有相关权限"),
    NOT_FOUND(404, "请求数据不存在"),
    SYSTEM_ERROR(500, "系统内部异常"),
    OPERATION_ERROR(501, "操作失败"),

    // 用户相关错误
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ACCOUNT_EXIST(1002, "账号已存在"),
    USER_ACCOUNT_DISABLED(1003, "账号已被禁用"),
    USER_PASSWORD_ERROR(1004, "密码错误"),

    // 房源相关错误
    HOUSE_NOT_FOUND(2001, "房源不存在"),
    HOUSE_STATUS_ERROR(2002, "房源状态错误"),
    
    // 数据分析相关错误
    ANALYSIS_PARAM_ERROR(3001, "分析参数错误"),
    REPORT_NOT_FOUND(3002, "分析报告不存在"),
    REPORT_GENERATE_ERROR(3003, "报告生成失败");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
} 