package com.festival.domain.booth.model;

public enum BoothType {
    FOOD_TRUCK("푸드트럭"),
    FLEA_MARKET("플리마켓"),
    PUB("주점");

    private final String value;

    BoothType(String value){
        this.value = value;
    }
    public static BoothType handleType(String type)
    {
        switch (type) {
            case "FOOD_TRUCK":
                return BoothType.FOOD_TRUCK;
            case "FLEA_MARKET":
                return BoothType.FLEA_MARKET;
            case "PUB":
                return BoothType.PUB;
            default:
                return null;
        }
    }

    public String getValue(){
        return value;
    }

}
