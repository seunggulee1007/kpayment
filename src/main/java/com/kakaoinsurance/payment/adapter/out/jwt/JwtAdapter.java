package com.kakaoinsurance.payment.adapter.out.jwt;

import com.kakaoinsurance.payment.application.port.out.member.AuthenticationJwtPort;
import com.kakaoinsurance.payment.common.annotations.PersistenceAdapter;
import com.kakaoinsurance.payment.domain.member.JwtToken;
import com.kakaoinsurance.payment.domain.member.Member;
import com.kakaoinsurance.payment.domain.member.MemberRole;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class JwtAdapter implements AuthenticationJwtPort {

    private final Jwt jwt;

    @Override
    public JwtToken authentication(Member member) {
        Jwt.Claims claims =
            Jwt.Claims.of(member.id(), member.memberId().getId(), member.email(), member.nickname(), member.roles().stream().map(
                MemberRole::name).toArray(String[]::new));
        return JwtToken.of(jwt.createAccessToken(claims), jwt.createRefreshToken(claims));
    }

}
