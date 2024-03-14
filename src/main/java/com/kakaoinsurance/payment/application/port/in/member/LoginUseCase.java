package com.kakaoinsurance.payment.application.port.in.member;

import com.kakaoinsurance.payment.domain.member.JwtToken;

public interface LoginUseCase {

    JwtToken login(LoginCommand loginCommand);

}
