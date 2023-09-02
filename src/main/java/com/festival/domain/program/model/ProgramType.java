package com.festival.domain.program.model;

import lombok.Getter;

@Getter
public enum ProgramType {

    EVENT("EVENT"),
    GAME("GAME");

    private final String value;

    ProgramType(String value) {
        this.value = value;
    }

    public static ProgramType handleType(String value) {
        return switch (value) {
            case "EVENT" -> EVENT;
            case "GAME" -> GAME;
            default -> null;
        };
    }
}
