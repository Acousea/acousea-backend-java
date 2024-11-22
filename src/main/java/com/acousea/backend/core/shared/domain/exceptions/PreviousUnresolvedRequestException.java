package com.acousea.backend.core.shared.domain.exceptions;

public class PreviousUnresolvedRequestException extends RuntimeException {
    public PreviousUnresolvedRequestException(String message) {
        super(message);
    }
}

