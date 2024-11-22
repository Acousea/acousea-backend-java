package com.acousea.backend.core.communicationSystem.domain.exceptions;


public class InvalidPacketException extends Exception {

    // Constructor por defecto
    public InvalidPacketException() {
        super("Invalid packet received.");
    }

    // Constructor que acepta un mensaje personalizado
    public InvalidPacketException(String message) {
        super(message);
    }

    // Constructor que acepta un mensaje y una causa
    public InvalidPacketException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor que acepta solo una causa
    public InvalidPacketException(Throwable cause) {
        super(cause);
    }
}

