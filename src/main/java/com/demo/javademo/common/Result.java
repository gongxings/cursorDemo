package com.demo.javademo.common;

public class Result<T> {
    private boolean success;
    private String message;
    private Integer code;
    private T data;

    public Result(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public Result(boolean success, String message,Integer code, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.code = code;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(true, "操作成功", ErrorCode.SUCCESS.getCode(), data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(true, message, ErrorCode.SUCCESS.getCode(), data);
    }

    public static <T> Result<T> success(String message) {
        return new Result<>(true, message, ErrorCode.SUCCESS.getCode(), null);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>(false, message, ErrorCode.OPERATION_ERROR.getCode(), null);
    }
    public static <T> Result<T> error(int code ,String message) {
        return new Result<>(false, message,code, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
} 