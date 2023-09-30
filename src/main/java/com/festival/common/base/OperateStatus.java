package com.festival.common.base;

import com.festival.common.exception.ErrorCode;
import com.festival.common.exception.custom_exception.InvalidException;
import lombok.Getter;

@Getter
public enum OperateStatus {

    OPERATE("OPERATE"),
    TERMINATE("TERMINATE"),
    UPCOMING("UPCOMING");

    private String value;

    OperateStatus(String value) {
        this.value = value;
    }

    public static OperateStatus checkStatus(String status) {
        return switch (status) {
            case "OPERATE" -> OperateStatus.OPERATE;
            case "TERMINATE" -> OperateStatus.TERMINATE;
            case "UPCOMING" -> OperateStatus.UPCOMING;
            default -> throw new InvalidException(ErrorCode.INVALID_STATUS);
        };
    }
}
