package com.acousea.backend.core.communicationSystem.domain.communication.constants;

import java.util.HashMap;
import java.util.Map;

public enum IridiumErrorCode {
    INVALID_LOGIN_CREDENTIALS(10, "Invalid login credentials"),
    NO_ROCKBLOCK_FOUND(11, "No RockBLOCK with this IMEI found on your account"),
    NO_LINE_RENTAL(12, "RockBLOCK has no line rental"),
    INSUFFICIENT_CREDIT(13, "Your account has insufficient credit"),
    DECODE_HEX_DATA_ERROR(14, "Could not decode hex data"),
    DATA_TOO_LONG(15, "Data too long"),
    NO_DATA(16, "No data"),
    SYSTEM_ERROR(99, "System Error");

    private final int code;
    private final String description;
    private static final Map<Integer, IridiumErrorCode> CODE_MAP = new HashMap<>();

    static {
        for (IridiumErrorCode errorCode : IridiumErrorCode.values()) {
            CODE_MAP.put(errorCode.getCode(), errorCode);
        }
    }

    IridiumErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static String getDescription(int code) {
        IridiumErrorCode errorCode = CODE_MAP.get(code);
        return errorCode != null ? errorCode.getDescription() : "Unknown error code";
    }
}

