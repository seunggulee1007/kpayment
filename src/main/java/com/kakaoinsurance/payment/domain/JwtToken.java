package com.kakaoinsurance.payment.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtToken {

    private String accessToken;
    private String refreshToken;

    public static JwtToken of(String accessToken, String refreshToken) {
        JwtToken jwtToken = new JwtToken();
        jwtToken.accessToken = accessToken;
        jwtToken.refreshToken = refreshToken;
        return jwtToken;
    }

}
