package com.festival.domain.member.model;

import com.festival.common.security.oauth.SocialCode;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@MappedSuperclass
@Getter
public abstract class AuthDetailsEntity implements UserDetails {

    @Column(unique = true)
    protected String username;

    @Column(unique = true)
    protected String email;

    @Column(nullable = false)
    protected String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    protected SocialCode socialCode;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    protected MemberRole role;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "member_roles", joinColumns = @JoinColumn(name = "member_id"))
    @Enumerated(EnumType.STRING)
    protected List<MemberRole> memberRoles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.memberRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}