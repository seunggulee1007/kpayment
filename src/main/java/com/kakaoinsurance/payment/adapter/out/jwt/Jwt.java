package com.kakaoinsurance.payment.adapter.out.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class Jwt {

    private final String issuer;

    private final Algorithm algorithm;

    private final JWTVerifier jwtVerifier;

    public Jwt(String issuer, String secretKey) {
        this.issuer = issuer;
        this.algorithm = Algorithm.HMAC512(secretKey);
        this.jwtVerifier = JWT.require(algorithm).withIssuer(issuer).build();
    }

    public String createToken(Claims claims, long expireTime) {
        Date now = new Date();
        JWTCreator.Builder builder = JWT.create();
        builder.withIssuer(issuer);
        builder.withIssuedAt(now);
        builder.withExpiresAt(new Date(now.getTime() + expireTime));
        builder.withClaim(JwtInfo.MEMBER_ID.name(), claims.memberId);
        builder.withClaim(JwtInfo.ID.name(), claims.id);
        builder.withClaim(JwtInfo.EMAIL.name(), claims.email);
        builder.withClaim(JwtInfo.NICKNAME.name(), claims.nickname);
        builder.withArrayClaim(JwtInfo.ROLES.name(), claims.roles);
        return builder.sign(algorithm);
    }

    public String createAccessToken(Claims claims) {
        // 5분 동안만 토큰 유효
        long tokenValidationSecond = 1000L * 60 * 5;
        return createToken(claims, tokenValidationSecond);
    }

    public String createRefreshToken(Claims claims) {
        // 30분 동안 유효한 리프레시 토큰
        long refreshTokenValidationSecond = 1000L * 60 * 60 * 24;
        return createToken(claims, refreshTokenValidationSecond);
    }

    public Claims verify(String token) throws JWTVerificationException {
        return new Claims(jwtVerifier.verify(token));
    }

    @Data
    public static class Claims {

        private Long id;
        private String memberId;
        private String email;
        private String nickname;
        private String[] roles;
        private Date iat;
        private Date exp;

        private Claims() {/*empty*/}

        Claims(DecodedJWT decodedJWT) {
            Claim idClaim = decodedJWT.getClaim(JwtInfo.ID.name());
            if (!idClaim.isNull()) {
                this.id = idClaim.asLong();
            }
            Claim memberIdClaim = decodedJWT.getClaim(JwtInfo.MEMBER_ID.name());
            if (!memberIdClaim.isNull()) {
                this.memberId = memberIdClaim.asString();
            }
            Claim emailClaim = decodedJWT.getClaim(JwtInfo.EMAIL.name());
            if (!emailClaim.isNull()) {
                this.email = emailClaim.asString();
            }

            Claim nicknameClaim = decodedJWT.getClaim(JwtInfo.NICKNAME.name());
            if (!nicknameClaim.isNull()) {
                this.nickname = nicknameClaim.asString();
            }

            Claim rolesClaim = decodedJWT.getClaim(JwtInfo.ROLES.name());
            if (!rolesClaim.isNull()) {
                this.roles = rolesClaim.asArray(String.class);
            }
            this.iat = decodedJWT.getIssuedAt();
            this.exp = decodedJWT.getExpiresAt();
        }

        public static Claims of(long userKey, String memberId, String email, String nickname, String[] roles) {
            Claims claims = new Claims();
            claims.id = userKey;
            claims.memberId = memberId;
            claims.email = email;
            claims.nickname = nickname;
            claims.roles = roles;
            return claims;
        }

    }

}