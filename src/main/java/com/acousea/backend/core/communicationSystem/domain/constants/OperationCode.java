package com.acousea.backend.core.shared.domain.constants;

public enum OperationCode {
    PING('0'),
    ERROR('1'),
    GET_PAM_DEVICE_INFO('E'),
    GET_PAM_DEVICE_STREAMING_CONFIG('r'),
    SET_PAM_DEVICE_STREAMING_CONFIG('R'),
    GET_PAM_DEVICE_LOGGING_CONFIG('l'),
    SET_PAM_DEVICE_LOGGING_CONFIG('L'),
    CHANGE_OP_MODE('O'),
    SUMMARY_REPORT('S'),
    SUMMARY_SIMPLE_REPORT('s'),
    SET_REPORTING_PERIODS('P'),
    GET_REPORTING_PERIODS('p');

    private final char value;

    OperationCode(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }

}
