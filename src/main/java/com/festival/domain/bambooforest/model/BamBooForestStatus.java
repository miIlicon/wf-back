package com.festival.domain.bambooforest.model;

import lombok.Getter;

@Getter
public enum BamBooForestStatus {

    OPERATE("OPERATE"),
    TERMINATE("TERMINATE");

    private String value;

    BamBooForestStatus(String value) {
        this.value = value;
    }

    public static BamBooForestStatus checkStatus(String status) {
        return switch (status) {
            case "OPERATE" -> BamBooForestStatus.OPERATE;
            case "TERMINATE" -> BamBooForestStatus.TERMINATE;
            default -> null;
        };
    }
}
