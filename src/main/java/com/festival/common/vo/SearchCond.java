package com.festival.common.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchCond {
    private Long userId;
    private Boolean state;

    public SearchCond(Long userId, Boolean state) {
        this.userId = userId;
        this.state = state;
    }

    public SearchCond(Long userId) {
        this.userId = userId;
    }
}
