package com.festival.domain.guide.notice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.festival.domain.guide.notice.model.Notice;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NoticeRes {

    private Long id;
    private String content;
    private String username;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdDateTime;

    @Builder
    private NoticeRes(Long id, String content, String username, LocalDateTime createdDateTime) {
        this.id = id;
        this.content = content;
        this.username = username;
        this.createdDateTime = createdDateTime;
    }

    public static NoticeRes of(Notice notice) {
        return NoticeRes.builder()
                .id(notice.getId())
                .content(notice.getContent())
                .username(notice.getMember().getUsername())
                .createdDateTime(notice.getCreatedDate())
                .build();
    }
}
