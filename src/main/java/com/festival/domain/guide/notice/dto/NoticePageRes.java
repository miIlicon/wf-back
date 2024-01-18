package com.festival.domain.guide.notice.dto;

import com.festival.domain.guide.notice.model.Notice;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class NoticePageRes {

    private List<NoticeRes> noticeResList;
    private long totalCount;
    private int totalPage;
    private int pageNumber;
    private int pageSize;

    @Builder
    private NoticePageRes(List<NoticeRes> noticeResList, long totalCount, int totalPage, int pageNumber, int pageSize) {
        this.noticeResList = noticeResList;
        this.totalCount = totalCount;
        this.totalPage = totalPage;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public static NoticePageRes of(Page<Notice> page){
        return NoticePageRes.builder()
                .noticeResList(page.getContent().stream().map(NoticeRes::of).collect(Collectors.toList()))
                .totalPage(page.getTotalPages())
                .totalCount(page.getTotalElements())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .build();
    }
}
