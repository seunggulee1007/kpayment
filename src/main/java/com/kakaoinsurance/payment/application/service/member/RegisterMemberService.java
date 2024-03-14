package com.kakaoinsurance.payment.application.service.member;

import com.kakaoinsurance.payment.application.port.in.member.RegisterMemberCommand;
import com.kakaoinsurance.payment.application.port.in.member.RegisterMemberUseCase;
import com.kakaoinsurance.payment.application.port.in.member.RegisteredMember;
import com.kakaoinsurance.payment.application.port.out.member.RegisterMemberOutPort;
import com.kakaoinsurance.payment.common.annotations.UseCase;
import com.kakaoinsurance.payment.domain.member.Member;
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
