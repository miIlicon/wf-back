package com.festival.domain.program.model;

public enum ProgramType {
    EVENT("이벤트"), GAME("경기");

    private final String itemType;

    ProgramType(String type) {
        this.itemType = type;
    }

    public static ProgramType handleType(String type) {
        return switch (type) {
            case "EVENT" -> EVENT;
            case "GAME" -> GAME;
            default -> null;
        };
    }
}
