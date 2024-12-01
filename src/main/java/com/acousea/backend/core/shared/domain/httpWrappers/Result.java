package com.acousea.backend.core.shared.domain.httpWrappers;

import java.util.List;

public record Result<T>(HttpError error, boolean success, T value) {

    public static <T> Result<T> fieldFail(String message, List<String> fields) {
        return new Result<>(new HttpError(400, message, fields), false, null);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(new HttpError(400, message, null), false, null);
    }

    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(new HttpError(code, message, null), false, null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(null, true, data);
    }

    public static <T> Result<T> success(T data, String message) {
        return new Result<>(null, true, data);
    }

}

