package com.festival.domain.guide.model;

import lombok.Getter;

@Getter
public enum GuideType {
    QNA("QNA"),
    NOTICE("NOTICE");

    private String value;

    GuideType(String value) {
        this.value = value;
    }

    public static GuideType checkType(String type) {
        return switch (type) {
            case "QNA" -> GuideType.QNA;
            case "NOTICE" -> GuideType.NOTICE;
            default -> null;
        };
    }
}
