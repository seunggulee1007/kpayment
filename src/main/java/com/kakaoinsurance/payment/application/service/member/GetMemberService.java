package com.kakaoinsurance.payment.application.service.member;

import com.kakaoinsurance.payment.application.port.in.member.GetMemberQuery;
import com.kakaoinsurance.payment.application.port.in.member.GetMemberUseCase;
import com.kakaoinsurance.payment.application.port.out.member.GetMemberPort;
import com.kakaoinsurance.payment.common.annotations.UseCase;
import com.kakaoinsurance.payment.domain.member.Member;
import lombok.RequiredArgsConstructor;

/**
 * 회원 조회 서비스
 *
 * @author seunggu.lee
 */
@UseCase
@RequiredArgsConstructor
public class GetMemberService implements GetMemberUseCase {

    private final GetMemberPort getMemberPort;

    @Override
    public Member getMember(GetMemberQuery getMemberQuery) {
        return getMemberPort.getMemberByIdAndPassword(getMemberQuery.getMemberId(), getMemberQuery.getPassword());
    }

}
