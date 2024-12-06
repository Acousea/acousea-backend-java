package com.acousea.backend.core.shared.domain.httpWrappers;

import java.util.List;

public record ApiResult<T>(HttpError error, boolean success, T value) {

    public static <T> ApiResult<T> fieldFail(String message, List<String> fields) {
        return new ApiResult<>(new HttpError(400, message, fields), false, null);
    }

    public static <T> ApiResult<T> fail(String message) {
        return new ApiResult<>(new HttpError(400, message, null), false, null);
    }

    public static <T> ApiResult<T> fail(int code, String message) {
        return new ApiResult<>(new HttpError(code, message, null), false, null);
    }

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(null, true, data);
    }

    public static <T> ApiResult<T> success(T data, String message) {
        return new ApiResult<>(null, true, data);
    }

}

