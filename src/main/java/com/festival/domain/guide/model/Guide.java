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

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private Long viewCount = 0L;

    @Builder
    private Guide(String content, boolean deleted) {
        this.content = content;
        this.deleted = deleted;
    }

    public static Guide of(GuideReq guideReq) {
        return Guide.builder()
                .content(guideReq.getContent())
                .deleted(false)
                .build();
    }

    public void update(GuideReq guideReq) {
        this.content = guideReq.getContent();
    }

    public void increaseViewCount(Long viewCount) {
        this.viewCount += viewCount;
    }

    public void connectMember(Member member) {
        this.member = member;
    }

    public void deletedGuide() {
        this.deleted = true;
    }

}
