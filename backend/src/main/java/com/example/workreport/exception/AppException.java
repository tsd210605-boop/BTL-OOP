package com.example.workreport.exception;


public class AppException extends RuntimeException {
    private String code;
    private int httpStatus;

    public AppException(String message) {
        super(message);
        this.code = "500";
        this.httpStatus = 500;
    }

    public AppException(String message, String code) {
        super(message);
        this.code = code;
        this.httpStatus = 500;
    }

    public AppException(String message, String code, int httpStatus) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
