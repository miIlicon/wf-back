package com.festival.domain.info.festivalPub.data.entity.pub;

import lombok.Getter;

@Getter
public enum PubState {

    CLOSED("CLOSED"),
    OPENED("OPENED");

    private final String value;

    PubState(String value) {
        this.value = value;
    }
}
