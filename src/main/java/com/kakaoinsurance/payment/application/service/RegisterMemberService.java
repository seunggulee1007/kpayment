package com.kakaoinsurance.payment.application.service;

import com.kakaoinsurance.payment.application.port.in.RegisterMemberCommand;
import com.kakaoinsurance.payment.application.port.in.RegisterMemberUseCase;
import com.kakaoinsurance.payment.application.port.in.RegisteredMember;
import com.kakaoinsurance.payment.application.port.out.RegisterMemberPort;
import com.kakaoinsurance.payment.common.UseCase;
import com.kakaoinsurance.payment.domain.Member;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class RegisterMemberService implements RegisterMemberUseCase {

    private final RegisterMemberPort registerMemberPort;

    @Override
    public RegisteredMember registerMember(RegisterMemberCommand registerMemberCommand) {
        Member member = registerMemberPort.registerMember(registerMemberCommand.toDomain());
        return RegisteredMember.mapToDto(member);
    }

}
