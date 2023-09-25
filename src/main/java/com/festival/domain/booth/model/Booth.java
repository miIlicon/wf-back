package com.festival.domain.booth.model;

import com.festival.common.base.BaseEntity;
import com.festival.common.base.OperateStatus;
import com.festival.domain.booth.controller.dto.BoothReq;
import com.festival.domain.image.model.Image;
import com.festival.domain.member.model.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Booth extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String subTitle;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private float latitude;

    @Column(nullable = false)
    private float longitude;

    @Column(nullable = false)
    private OperateStatus operateStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BoothType type;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private Long viewCount = 0L;

    @Builder
    private Booth(String title, String subTitle, String content, float latitude, float longitude, boolean deleted, OperateStatus operateStatus, BoothType type) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.operateStatus = operateStatus;
        this.type = type;
        this.deleted = deleted;
    }

    public static Booth of(BoothReq boothReq){
        return Booth.builder()
                .title(boothReq.getTitle())
                .subTitle(boothReq.getSubTitle())
                .content(boothReq.getContent())
                .latitude(boothReq.getLatitude())
                .longitude(boothReq.getLongitude())
                .operateStatus(OperateStatus.checkStatus(boothReq.getStatus()))
                .deleted(false)
                .type(BoothType.handleType(boothReq.getType())).build();
    }
    public void setImage(Image image){
        this.image = image;
    }

    public void connectMember(Member member){
        this.member = member;
    }

    public void update(BoothReq boothReq){
        this.title = boothReq.getTitle();
        this.subTitle = boothReq.getSubTitle();
        this.content =  boothReq.getContent();
        this.latitude = boothReq.getLatitude();
        this.longitude = boothReq.getLongitude();
        this.operateStatus = OperateStatus.checkStatus(boothReq.getStatus());
        this.type = BoothType.handleType(boothReq.getType());
    }

    public void changeStatus() {
        if(this.operateStatus.getValue().equals("OPERATE"))
            this.operateStatus = OperateStatus.TERMINATE;
        else
            this.operateStatus = OperateStatus.OPERATE;
    }
    public void deleteBooth(){
        this.deleted = true;
    }

    public void increaseViewCount(Long viewCount) {
        this.viewCount += viewCount;
    }

    public void decreaseViewCount(Long viewCount) {
        this.viewCount -= viewCount;
    }
}
