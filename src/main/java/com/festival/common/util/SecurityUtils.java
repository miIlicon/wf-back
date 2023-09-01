package com.festival.common.util;

import com.festival.domain.member.model.MemberRole;

import java.util.List;

public class SecurityUtils {
    public static boolean checkingRole(String accessorUsername, String ownerUsername, List<MemberRole> accessorRoles) {
        return accessorRoles.contains(MemberRole.ADMIN) || accessorUsername.equals(ownerUsername);
    }
    public static boolean checkingRole(List<MemberRole> memberRoles) {
        return memberRoles.contains(MemberRole.ADMIN);
    }
}
