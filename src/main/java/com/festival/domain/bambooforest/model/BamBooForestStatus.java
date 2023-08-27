package com.festival.domain.bambooforest.model;

import lombok.Getter;

@Getter
public enum BamBooForestStatus {

    OPERATE("개시"),
    TERMINATE("삭제");

    private String value;

    BamBooForestStatus(String value) {
        this.value = value;
    }

    public static BamBooForestStatus checkStatus(String status) {
        return switch (status) {
            case "개시" -> BamBooForestStatus.OPERATE;
            case "삭제" -> BamBooForestStatus.TERMINATE;
            default -> null;
        };
    }
}
