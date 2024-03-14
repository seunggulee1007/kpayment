package com.kakaoinsurance.payment.application.port.out.member;

import com.kakaoinsurance.payment.domain.member.JwtToken;
import com.kakaoinsurance.payment.domain.member.Member;

public interface AuthenticationJwtPort {

    JwtToken authentication(Member member);

}
