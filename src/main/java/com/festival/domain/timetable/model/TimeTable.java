package com.festival.domain.timetable.model;

import com.festival.common.base.BaseEntity;
import com.festival.domain.timetable.dto.TimeTableCreateReq;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TimeTable extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimeTableStatus timeTableStatus;

    @Builder
    private TimeTable(String title, LocalDateTime startTime, LocalDateTime endTime, TimeTableStatus timeTableStatus) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timeTableStatus = timeTableStatus;
    }

    public static TimeTable of(TimeTableCreateReq timeTableCreateReq) {
        return TimeTable.builder()
                .title(timeTableCreateReq.getTitle())
                .startTime(timeTableCreateReq.getStartTime())
                .endTime(timeTableCreateReq.getEndTime())
                .timeTableStatus(settingStatus(timeTableCreateReq.getStatus()))
                .build();
    }

    public void update(TimeTableCreateReq timeTableCreateReq) {
        this.title = timeTableCreateReq.getTitle();
        this.startTime = timeTableCreateReq.getStartTime();
        this.endTime = timeTableCreateReq.getEndTime();
        this.timeTableStatus = settingStatus(timeTableCreateReq.getStatus());
    }

    public void changeStatus(TimeTableStatus timeTableStatus) {
        this.timeTableStatus = timeTableStatus;
    }

    private static TimeTableStatus settingStatus(String timeTableStatus) {
        return TimeTableStatus.checkStatus(timeTableStatus);
    }
}
