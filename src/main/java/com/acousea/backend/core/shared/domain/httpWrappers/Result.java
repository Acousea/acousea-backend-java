package com.acousea.backend.core.shared.domain.httpWrappers;

import java.util.List;

public record Result<T>(HttpError error, T success) {

    public static <T> Result<T> fieldFail(String message, List<String> fields) {
        return new Result<>(new HttpError(400, message, fields), null);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(new HttpError(400, message, null), null);
    }

    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(new HttpError(code, message, null), null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(null, data);
    }

    public static <T> Result<T> success(T data, String message) {
        return new Result<>(null, data);
    }

}

