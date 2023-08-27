package com.festival.domain.bambooforest.model;

import com.festival.common.base.BaseEntity;
import com.festival.domain.bambooforest.dto.BamBooForestCreateReq;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BamBooForest extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    private String contact;

    private BamBooForestStatus bamBooForestStatus;

    @Builder
    private BamBooForest(String content, String contact, BamBooForestStatus bamBooForestStatus) {
        this.content = content;
        this.contact = contact;
        this.bamBooForestStatus = bamBooForestStatus;
    }

    public static BamBooForest of(BamBooForestCreateReq bambooForestCreateReq) {
        return BamBooForest.builder()
                .content(bambooForestCreateReq.getContent())
                .contact(bambooForestCreateReq.getContact())
                .bamBooForestStatus(settingStatus(bambooForestCreateReq.getStatus()))
                .build();
    }

    public void changeStatus(BamBooForestStatus bamBooForestStatus) {
        this.bamBooForestStatus = bamBooForestStatus;
    }

    private static BamBooForestStatus settingStatus(String status) {
        return BamBooForestStatus.checkStatus(status);
    }
}
