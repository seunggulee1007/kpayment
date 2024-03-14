package com.kakaoinsurance.payment.application.port.in.member;

import com.kakaoinsurance.payment.domain.member.Member;

public interface GetMemberUseCase {

    Member getMember(GetMemberQuery getMemberQuery);

}
