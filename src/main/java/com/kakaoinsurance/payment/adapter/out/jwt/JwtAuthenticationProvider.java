package com.kakaoinsurance.payment.adapter.out.jwt;

import com.kakaoinsurance.payment.application.port.in.member.GetMemberQuery;
import com.kakaoinsurance.payment.application.port.in.member.GetMemberUseCase;
import com.kakaoinsurance.payment.domain.member.Member;
import com.kakaoinsurance.payment.domain.member.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 로그인 시 인증 객체를 Authentication 에 담아주는 클래스
 *
 * @author seunggu.lee
 * @see AuthenticationProvider#authenticate(Authentication)
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final GetMemberUseCase getMemberUseCase;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken)authentication;
        return processUserAuthentication((JwtAuthentication)authenticationToken.getPrincipal(), authenticationToken.getCredentials());
    }

    private Authentication processUserAuthentication(JwtAuthentication principal, CredentialInfo credential) {
        try {
            Member member = getMember(principal.id(), credential);
            CredentialInfo credentialInfo = new CredentialInfo(member.password());
            JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(
                new JwtAuthentication(member.id(), member.memberId().getId(), member.email(), member.nickname()),
                credentialInfo,
                this.authorities(member.roles()));
            authenticationToken.setDetails(member);
            return authenticationToken;
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException(e.getMessage());
        } catch (DataAccessException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JwtAuthenticationToken.class);
    }

    private Member getMember(Long memberId, CredentialInfo credential) {
        return getMemberUseCase.getMember(GetMemberQuery.of(memberId, credential.getCredential()));
    }

    private Collection<? extends GrantedAuthority> authorities(Set<MemberRole> role) {
        return role.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r.name())).collect(Collectors.toSet());
    }

}