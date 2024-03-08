package com.kakaoinsurance.payment.application.service;

import com.kakaoinsurance.payment.application.port.in.GetMemberQuery;
import com.kakaoinsurance.payment.application.port.in.GetMemberUseCase;
import com.kakaoinsurance.payment.application.port.out.GetMemberPort;
import com.kakaoinsurance.payment.common.annotations.UseCase;
import com.kakaoinsurance.payment.domain.Member;
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
        return getMemberPort.getMemberByIdAndPassword(getMemberQuery.getAccountId(), getMemberQuery.getPassword());
    }

}
