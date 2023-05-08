package com.festival.domain.info.festivalPub.data.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PubSearchCond {

    private Long userId;
    private Boolean state;

    public PubSearchCond(Long userId, Boolean state) {
        this.userId = userId;
        this.state = state;
    }
}
