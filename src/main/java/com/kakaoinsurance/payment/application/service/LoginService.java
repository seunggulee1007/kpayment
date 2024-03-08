package com.kakaoinsurance.payment.application.service;

import com.kakaoinsurance.payment.application.port.in.LoginCommand;
import com.kakaoinsurance.payment.application.port.in.LoginUseCase;
import com.kakaoinsurance.payment.application.port.out.AuthenticationJwtPort;
import com.kakaoinsurance.payment.application.port.out.GetMemberPort;
import com.kakaoinsurance.payment.common.UseCase;
import com.kakaoinsurance.payment.domain.JwtToken;
import com.kakaoinsurance.payment.domain.Member;
import lombok.RequiredArgsConstructor;

/**
 * 로그인을 구현한 객체
 * 로그인이 완료되면 유효한 토큰이 발행된다.
 *
 * @author seunggu.lee
 */
@UseCase
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {

    private final GetMemberPort getMemberPort;
    private final AuthenticationJwtPort authenticationJwtPort;

    @Override
    public JwtToken login(LoginCommand loginCommand) {
        Member member =
            getMemberPort.getMemberByEmailAndPassword(loginCommand.getEmail(), loginCommand.getPassword());
        return authenticationJwtPort.authentication(member);
    }

}
