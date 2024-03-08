package com.kakaoinsurance.payment.common.security;

import com.kakaoinsurance.payment.adapter.out.jwt.JwtAuthentication;
import com.kakaoinsurance.payment.adapter.out.jwt.JwtAuthenticationToken;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LoginAccountAuditorAware implements AuditorAware<String> {

    @Nonnull
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        if (authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken)authentication;
        JwtAuthentication jwtAuthentication = (JwtAuthentication)authenticationToken.getPrincipal();
        return Optional.of(jwtAuthentication.memberId());
    }

}