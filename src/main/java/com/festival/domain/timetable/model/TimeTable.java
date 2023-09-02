package com.festival.domain.timetable.model;

import com.festival.common.base.BaseEntity;
import com.festival.common.base.OperateStatus;
import com.festival.domain.member.model.Member;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    private TimeTable(String title, LocalDateTime startTime, LocalDateTime endTime, OperateStatus status) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public static TimeTable of(TimeTableCreateReq timeTableCreateReq) {
        return TimeTable.builder()
                .title(timeTableCreateReq.getTitle())
                .startTime(timeTableCreateReq.getStartTime())
                .endTime(timeTableCreateReq.getEndTime())
                .status(settingStatus(timeTableCreateReq.getStatus()))
                .build();
    }

    public void update(TimeTableCreateReq timeTableCreateReq) {
        this.title = timeTableCreateReq.getTitle();
        this.startTime = timeTableCreateReq.getStartTime();
        this.endTime = timeTableCreateReq.getEndTime();
        this.status = settingStatus(timeTableCreateReq.getStatus());
    }

    public void changeStatus(OperateStatus timeTableStatus) {
        this.status = timeTableStatus;
    }

    public void connectMember(Member member) {
        this.member = member;
    }

    private static OperateStatus settingStatus(String status) {
        return OperateStatus.checkStatus(status);
    }
}
