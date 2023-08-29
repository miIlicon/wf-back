package com.festival.domain.guide.model;

import lombok.Getter;

@Getter
public enum GuideType {
    QNA("Q&A"),
    NOTICE("공지사항");

    private String value;

    GuideType(String value) {
        this.value = value;
    }

    public static GuideType checkType(String type) {
        return switch (type) {
            case "Q&A" -> GuideType.QNA;
            case "notice" -> GuideType.NOTICE;
            default -> null;
        };
    }
}
