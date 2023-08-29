package com.festival.domain.guide.model;

import lombok.Getter;

@Getter
public enum GuideStatus {
    OPERATE("운영중"),
    TERMINATE("종료");

    private String value;

    GuideStatus(String value) {
        this.value = value;
    }

    public static GuideStatus checkStatus(String status) {
        return switch (status) {
            case "OPERATE" -> GuideStatus.OPERATE;
            case "TERMINATE" -> GuideStatus.TERMINATE;
            default -> null;
        };
    }
}
