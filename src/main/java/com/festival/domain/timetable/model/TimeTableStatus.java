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
        for (TimeTableStatus timeTableStatus : TimeTableStatus.values()) {
            if (timeTableStatus.value.equals(status)) {
                return timeTableStatus;
            }
        }
        return null;
    }
}
