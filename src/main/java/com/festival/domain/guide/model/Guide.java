package com.festival.domain.guide.model;

import com.festival.common.base.BaseEntity;
import com.festival.common.base.OperateStatus;
import com.festival.domain.guide.dto.GuideReq;
import com.festival.domain.image.model.Image;
import com.festival.domain.member.model.Member;
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
    private GuideType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Image image;

    @Builder
    private Guide(String title, String content, GuideType type, OperateStatus status) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.status = status;
    }

    public static Guide of(GuideReq guideReq) {
        return Guide.builder()
                .title(guideReq.getTitle())
                .content(guideReq.getContent())
                .type(settingType(guideReq.getType()))
                .status(settingStatus(guideReq.getStatus()))
                .build();
    }

    public void update(GuideReq guideReq) {
        this.title = guideReq.getTitle();
        this.content = guideReq.getContent();
        this.type = settingType(guideReq.getType());
    }

    public void setImage(Image image){
        this.image = image;
    }

    public void connectMember(Member member) {
        this.member = member;
    }

    public void changeStatus(OperateStatus status) {
        this.status = status;
    }

    private static GuideType settingType(String guideType) {
        return GuideType.checkType(guideType);
    }

    private static OperateStatus settingStatus(String guideStatus) {
        return OperateStatus.checkStatus(guideStatus);
    }
}
