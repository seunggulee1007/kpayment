package com.kakaoinsurance.payment.application.port.in;

import com.kakaoinsurance.payment.domain.JwtToken;

public interface LoginUseCase {

    JwtToken login(LoginCommand loginCommand);

}
