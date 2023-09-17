package com.festival.common.base;

import lombok.Getter;

@Getter
public enum OperateStatus {

    OPERATE("OPERATE"),
    TERMINATE("TERMINATE");

    private String value;

    OperateStatus(String value) {
        this.value = value;
    }

    public static OperateStatus checkStatus(String status) {
        return switch (status) {
            case "OPERATE" -> OperateStatus.OPERATE;
            case "TERMINATE" -> OperateStatus.TERMINATE;
            default -> null;
        };
    }
}
