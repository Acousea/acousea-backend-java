package com.acousea.backend.core.shared.domain;
import java.util.List;

public class HttpResponse<T> {
    private int code;
    private String message;
    private T data;

    public HttpResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> HttpResponse<T> fieldFail(String message, List<String> fields) {
        return new HttpResponse<>(400, message, null);
    }

    public static <T> HttpResponse<T> fail(String message) {
        return new HttpResponse<>(500, message, null);
    }

    public static <T> HttpResponse<T> fail(int code, String message) {
        return new HttpResponse<>(code, message, null);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}

