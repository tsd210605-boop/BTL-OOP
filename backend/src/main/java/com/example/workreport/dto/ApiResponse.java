package com.example.workreport.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private String code;

    public ApiResponse() {}

    public ApiResponse(boolean success, String message, T data, String code) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.code = code;
    }

    public boolean isSuccess() {return success;}
    public void setSuccess(boolean success) {this.success = success;}
    public String getMessage() {return message;}
    public void setMessage(String message) {this.message = message;}
    public T getData() {return data;}
    public void setData(T data) {this.data = data;}
    public String getCode() {return code;}
    public void setCode(String code) {this.code = code;}

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, "Thành công", data, "200");
    }

    public static <T> ApiResponse<T> ok(T data, String message) {
        return new ApiResponse<>(true, message, data, "200");
    }

    public static <T> ApiResponse<T> error(String message, String code) {
        return new ApiResponse<>(false, message, null, code);
    }

    public static <T> ApiResponse<T> error(String message, String code, T data) {
        return new ApiResponse<>(false, message, data, code);
    }

    public static <T> ApiResponse<T> badRequest(String message) {
        return error(message, "400");
    }
    public static <T> ApiResponse<T> unauthorized(String message) {
        return error(message, "401");
    }
    public static <T> ApiResponse<T> forbidden(String message) {
        return error(message, "403");
    }
    public static <T> ApiResponse<T> notFound(String message) {
        return error(message, "404");
    }
    public static <T> ApiResponse<T> serverError(String message) {
        return error(message, "500");
    }
}


