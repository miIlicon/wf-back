package com.festival.domain.booth.model;

public enum BoothStatus {
    OPERATE("운영중"),
    TERMINATE("종료");

    private final String value;

    BoothStatus(String value){
        this.value = value;
    }
    public static BoothStatus handleStatus(String status)
    {
        switch (status) {
            case "OPERATE":
                return BoothStatus.OPERATE;
            case "TERMINATE":
                return BoothStatus.TERMINATE;
            default:
                return null;
        }
    }

    public String getValue(){
        return value;
    }

}
