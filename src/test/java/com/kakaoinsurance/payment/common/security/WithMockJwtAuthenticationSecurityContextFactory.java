package com.kakaoinsurance.payment.common.security;

import com.kakaoinsurance.payment.adapter.out.jwt.JwtAuthentication;
import com.kakaoinsurance.payment.adapter.out.jwt.JwtAuthenticationToken;
import com.kakaoinsurance.payment.common.annotations.WithMockJwtAuthentication;
import com.kakaoinsurance.payment.domain.member.MemberRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class WithMockJwtAuthenticationSecurityContextFactory implements WithSecurityContextFactory<WithMockJwtAuthentication> {

    @Override
    public SecurityContext createSecurityContext(WithMockJwtAuthentication annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        JwtAuthenticationToken authentication = new JwtAuthenticationToken(
            new JwtAuthentication(annotation.id(), annotation.memberId(), annotation.email(), annotation.nickname()), null,
            authorities(Set.of(MemberRole.USER)));
        context.setAuthentication(authentication);
        return context;
    }

    private Collection<? extends GrantedAuthority> authorities(Set<MemberRole> role) {
        return role.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r.name())).collect(Collectors.toSet());
    }

}