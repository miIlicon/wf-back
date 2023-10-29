package com.festival.domain.program.model;


import com.festival.common.base.BaseEntity;
import com.festival.common.base.OperateStatus;
import com.festival.domain.image.model.Image;
import com.festival.domain.member.model.Member;
import com.festival.domain.program.dto.ProgramReq;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Program extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "sub_title", nullable = false)
    private String subTitle;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "latitude", nullable = false)
    private float latitude;

    @Column(name = "longitude", nullable = false)
    private float longitude;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProgramType type;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private Long viewCount = 0L;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OperateStatus operateStatus;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Builder
    private Program(Long id, String title, String subTitle, String content, float latitude, float longitude, OperateStatus operateStatus, ProgramType type, boolean deleted,
                    LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.operateStatus = operateStatus;
        this.type = type;
        this.deleted = deleted;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Program of(ProgramReq programReq, LocalDate dateTime) {
        return Program.builder()
                .title(programReq.getTitle())
                .subTitle(programReq.getSubTitle())
                .content(programReq.getContent())
                .latitude(programReq.getLatitude())
                .longitude(programReq.getLongitude())
                .operateStatus(resolveOperateStatus(programReq, dateTime))
                .type(ProgramType.handleType(programReq.getType()))
                .deleted(false)
                .startDate(programReq.getStartDate())
                .endDate(programReq.getEndDate())
                .build();
    }

    public void update(ProgramReq programReq) {
        this.title = programReq.getTitle();
        this.subTitle = programReq.getSubTitle();
        this.content = programReq.getContent();
        this.latitude = programReq.getLatitude();
        this.longitude = programReq.getLongitude();
        this.operateStatus = OperateStatus.checkStatus(programReq.getOperateStatus());
        this.type = ProgramType.handleType(programReq.getType());
        this.startDate = programReq.getStartDate();
        this.endDate = programReq.getEndDate();
    }

    public void changeStatus(String operateStatus) {
        this.operateStatus = OperateStatus.checkStatus(operateStatus);
    }

    public void deletedProgram() {
        this.deleted = true;
    }

    public void setImage(Image uploadImage) {
        this.image = uploadImage;
    }

    public void connectMember(Member member){
        this.member = member;
    }

    public void increaseViewCount(Long viewCount) {
        this.viewCount += viewCount;
    }

    private static OperateStatus resolveOperateStatus(ProgramReq programReq, LocalDate dateTime) {
        if (dateTime.isBefore(programReq.getStartDate())) {
            return OperateStatus.UPCOMING;
        } else if (dateTime.isAfter(programReq.getEndDate())) {
            return OperateStatus.TERMINATE;
        }
        return OperateStatus.checkStatus(programReq.getOperateStatus());
    }

}
