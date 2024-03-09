package com.kakaoinsurance.payment.application.port.out;

import com.kakaoinsurance.payment.domain.JwtToken;
import com.kakaoinsurance.payment.domain.Member;

public interface AuthenticationJwtPort {

    JwtToken authentication(Member member);

}
