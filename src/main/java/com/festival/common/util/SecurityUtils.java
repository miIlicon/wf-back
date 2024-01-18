package com.festival.common.util;

import com.festival.domain.member.model.Member;
import com.festival.domain.member.model.MemberRole;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SecurityUtils {

    public static boolean checkingRole(Member owner, Member accessMember) {
        if(SecurityUtils.checkingAdminRole(accessMember.getRole()) ||
                SecurityUtils.checkingManagerRole(accessMember.getRole()) && owner.getUsername().equals(accessMember.getUsername())) {
            return true;
        }
        return false;
    }

    public static boolean checkingManagerRole(MemberRole accessorRole) {
        return accessorRole.equals(MemberRole.MANAGER);
    }

    public static boolean checkingAdminRole(MemberRole memberRole) {
        return memberRole.equals(MemberRole.ADMIN);
    }

    public static boolean checkingManagerRoles(List<MemberRole> accessorRoles) {
        return accessorRoles.contains(MemberRole.MANAGER);
    }

    public static boolean checkingAdminRoles(List<MemberRole> memberRoles) {
        return memberRoles.contains(MemberRole.ADMIN);
    }

}
