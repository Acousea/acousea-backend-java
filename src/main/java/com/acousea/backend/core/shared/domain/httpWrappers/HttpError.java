package com.acousea.backend.core.shared.domain.httpWrappers;

import java.util.List;

public record HttpError(
        int error_code,
        String error_message,
        List<String> error_field) {
}

