package com.kakaoinsurance.payment.application.service;

import com.kakaoinsurance.payment.application.port.in.RegisterMemberCommand;
import com.kakaoinsurance.payment.application.port.in.RegisterMemberUseCase;
import com.kakaoinsurance.payment.application.port.in.RegisteredMember;
import com.kakaoinsurance.payment.application.port.out.RegisterMemberOutPort;
import com.kakaoinsurance.payment.common.annotations.UseCase;
import com.kakaoinsurance.payment.domain.Member;
import lombok.RequiredArgsConstructor;

/**
 * 회원 등록 서비스
 *
 * @author seunggu.lee
 */
@UseCase
@RequiredArgsConstructor
public class RegisterMemberService implements RegisterMemberUseCase {

    private final RegisterMemberOutPort registerMemberOutPort;

    @Override
    public RegisteredMember registerMember(RegisterMemberCommand registerMemberCommand) {
        Member member = registerMemberOutPort.registerMember(registerMemberCommand.toDomain());
        return RegisteredMember.mapToDto(member);
    }

}
