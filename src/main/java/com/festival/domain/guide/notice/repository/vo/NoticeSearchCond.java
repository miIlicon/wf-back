package com.festival.domain.guide.notice.repository.vo;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
public class NoticeSearchCond {

    private Pageable pageable;

    @Builder
    private NoticeSearchCond(Pageable pageable) {
        this.pageable = pageable;
    }

}
