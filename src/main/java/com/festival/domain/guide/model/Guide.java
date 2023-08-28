package com.festival.domain.guide.model;

import com.festival.common.base.BaseEntity;
import com.festival.domain.guide.dto.GuideReq;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Guide extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private GuideType guideType;

    private GuideStatus guideStatus;

    @Builder
    private Guide(String title, String content, GuideType guideType, GuideStatus guideStatus) {
        this.title = title;
        this.content = content;
        this.guideType = guideType;
        this.guideStatus = guideStatus;
    }

    public static Guide of(GuideReq guideReq) {
        return Guide.builder()
                .title(guideReq.getTitle())
                .content(guideReq.getContent())
                .guideType(settingType(guideReq.getType()))
                .guideStatus(settingStatus(guideReq.getStatus()))
                .build();
    }

    public void update(GuideReq guideReq) {
        this.title = guideReq.getTitle();
        this.content = guideReq.getContent();
        this.guideType = settingType(guideReq.getType());

    }

    public void changeStatus(GuideStatus status) {
        this.guideStatus = status;
    }

    private static GuideType settingType(String guideType) {
        return GuideType.checkType(guideType);
    }

    private static GuideStatus settingStatus(String guideStatus) {
        return GuideStatus.checkStatus(guideStatus);
    }
}
