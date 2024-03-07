package com.kakaoinsurance.payment.adapter.out.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Bean
    public Jwt jwt(JwtProperties jwtProperties) {
        return new Jwt(jwtProperties.getIssuer(), jwtProperties.getClientSecret());
    }

}
