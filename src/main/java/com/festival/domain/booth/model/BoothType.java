package com.festival.domain.booth.model;

import lombok.Getter;

@Getter
public enum BoothType {
    FOOD_TRUCK("FOOD_TRUCK"),
    FLEA_MARKET("FLEA_MARKET"),
    PUB("PUB");

    private final String value;

    BoothType(String value){
        this.value = value;
    }

    public static BoothType handleType(String type) {
        return switch (type) {
            case "FOOD_TRUCK" -> BoothType.FOOD_TRUCK;
            case "FLEA_MARKET" -> BoothType.FLEA_MARKET;
            case "PUB" -> BoothType.PUB;
            default -> null;
        };
    }
}
