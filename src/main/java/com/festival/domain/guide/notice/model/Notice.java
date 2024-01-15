package com.festival.domain.guide.notice.model;

import com.festival.common.base.BaseEntity;
import com.festival.domain.guide.notice.dto.NoticeReq;
import com.festival.domain.member.model.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Notice extends BaseEntity {

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
    private Notice(String content, boolean deleted) {
        this.content = content;
        this.deleted = deleted;
    }

    public static Notice of(NoticeReq noticeReq) {
        return Notice.builder()
                .content(noticeReq.getContent())
                .deleted(false)
                .build();
    }

    public void update(NoticeReq noticeReq) {
        this.content = noticeReq.getContent();
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
