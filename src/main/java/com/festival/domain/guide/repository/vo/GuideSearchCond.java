package com.festival.domain.guide.repository.vo;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
public class GuideSearchCond {

    private Pageable pageable;

    @Builder
    private GuideSearchCond(Pageable pageable) {
        this.pageable = pageable;
    }

}
