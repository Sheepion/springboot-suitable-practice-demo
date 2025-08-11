package com.sheepion.demo.common;

import lombok.Data;

/**
 * This is a common result class for the API response.
 */
@Data
public class Result {
    private int code;
    private String message;
    private Object data;

    public Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Result success(Object data) {
        return new Result(200, "success", data);
    }
    public static Result success(String message, Object data) {
        return new Result(200, message, data);
    }
    public static Result error(String message) {
        return new Result(500, message, null);
    }
    public static Result error(String message, Object data) {
        return new Result(500, message, data);
    }
    public static Result error(int code, String message) {
        return new Result(code, message, null);
    }
    
}