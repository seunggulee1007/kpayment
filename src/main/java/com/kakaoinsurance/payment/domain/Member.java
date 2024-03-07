package com.kakaoinsurance.payment.domain;

import lombok.Builder;

import java.util.Set;

@Builder
public record Member(
    Long id,
    MemberId memberId,
    String nickname,
    String password,
    String email,
    int loginCount,
    int loginFailCount,
    Set<MemberRole> roles) {

}
