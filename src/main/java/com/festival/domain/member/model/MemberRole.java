package com.festival.domain.member.model;

import lombok.Getter;

@Getter
public enum MemberRole {
    ADMIN("ADMIN");

    private String value;

    MemberRole(String value) {
        this.value = value;
    }

    public static MemberRole checkMemberRole(String role) {
        return switch (role) {
            case "ADMIN" -> ADMIN;
            default -> null;
        };
    }
}
