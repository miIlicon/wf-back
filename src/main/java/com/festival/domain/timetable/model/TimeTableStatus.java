package com.festival.domain.timetable.model;

import lombok.Getter;

@Getter
public enum TimeTableStatus {

    OPERATE("운영중"),
    TERMINATE("종료");

    private String value;

    TimeTableStatus(String value) {
        this.value = value;
    }

    public static TimeTableStatus checkStatus(String status) {
        return switch (status) {
            case "운영중" -> TimeTableStatus.OPERATE;
            case "종료" -> TimeTableStatus.TERMINATE;
            default -> null;
        };
    }
}
