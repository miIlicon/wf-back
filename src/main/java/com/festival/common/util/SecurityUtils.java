package com.festival.common.util;

import com.festival.common.exception.custom_exception.BadRequestException;
import com.festival.common.exception.custom_exception.NotFoundException;
import com.festival.domain.guide.model.Guide;
import com.festival.domain.member.model.MemberRole;

import java.util.List;


public class SecurityUtils {

    public static boolean checkingRole(String accessorUsername, String ownerUsername, List<MemberRole> accessorRoles) {
        return checkingAdmin(accessorRoles) || accessorUsername.equals(ownerUsername);
    }
    public static boolean checkingAdmin(List<MemberRole> memberRoles) {
        return memberRoles.contains(MemberRole.ADMIN);
    }
}
