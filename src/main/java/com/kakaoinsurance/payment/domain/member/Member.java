package com.kakaoinsurance.payment.domain.member;

import lombok.Builder;

import java.util.Set;

@Builder
public record Member(
    // 회원 식별자 ( 인덱스 용 )
    Long id,
    // 회원 식별자 ( 유일 키 )
    MemberId memberId,
    // 별명
    String nickname,
    // 비밀번호
    String password,
    // 이메일
    String email,
    // 권한 set
    Set<MemberRole> roles) {

}
