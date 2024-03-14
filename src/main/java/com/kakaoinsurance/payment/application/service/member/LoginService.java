package com.kakaoinsurance.payment.application.service.member;

import com.kakaoinsurance.payment.application.port.in.member.LoginCommand;
import com.kakaoinsurance.payment.application.port.in.member.LoginUseCase;
import com.kakaoinsurance.payment.application.port.out.member.AuthenticationJwtPort;
import com.kakaoinsurance.payment.application.port.out.member.GetMemberPort;
import com.kakaoinsurance.payment.common.annotations.UseCase;
import com.kakaoinsurance.payment.domain.member.JwtToken;
import com.kakaoinsurance.payment.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(readOnly = true)
    public JwtToken login(LoginCommand loginCommand) {
        Member member =
            getMemberPort.getMemberByEmailAndPassword(loginCommand.getEmail(), loginCommand.getPassword());
        return authenticationJwtPort.authentication(member);
    }

}
