package com.festival.domain.bambooforest.model;

import com.festival.common.base.BaseEntity;
import com.festival.common.base.OperateStatus;
import com.festival.domain.bambooforest.dto.BamBooForestReq;
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
    private BamBooForest(String content, String contact, boolean deleted) {
        this.content = content;
        this.contact = contact;
        this.deleted = deleted;
    }

    public static BamBooForest of(BamBooForestReq bambooForestReq) {
        return BamBooForest.builder()
                .content(bambooForestReq.getContent())
                .contact(bambooForestReq.getContact())
                .deleted(false)
                .build();
    }

    public void deletedBambooForest() {
        this.deleted = true;
    }

}
