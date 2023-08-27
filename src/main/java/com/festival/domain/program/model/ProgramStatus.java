package com.festival.domain.program.model;

public enum ProgramStatus {
    OPERATE("운영중"), TERMINATE("종료");

    final private String itemStatus;

    ProgramStatus(String status) {
        this.itemStatus = status;
    }

    public static ProgramStatus handleStatus(String status) {
        return switch (status) {
            case "OPERATE" -> OPERATE;
            case "TERMINATE" -> TERMINATE;
            default -> null;
        };
    }
    
}
