package com.festival.domain.bambooforest.model;

import com.festival.common.base.BaseEntity;
import com.festival.common.base.OperateStatus;
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

    @Builder
    private BamBooForest(String content, String contact, OperateStatus status) {
        this.content = content;
        this.contact = contact;
        this.status = status;
    }

    public static BamBooForest of(BamBooForestCreateReq bambooForestCreateReq) {
        return BamBooForest.builder()
                .content(bambooForestCreateReq.getContent())
                .contact(bambooForestCreateReq.getContact())
                .status(settingStatus(bambooForestCreateReq.getStatus()))
                .build();
    }

    public void changeStatus(OperateStatus bamBooForestStatus) {
        this.status = bamBooForestStatus;
    }

    private static OperateStatus settingStatus(String status) {
        return OperateStatus.checkStatus(status);
    }
}
