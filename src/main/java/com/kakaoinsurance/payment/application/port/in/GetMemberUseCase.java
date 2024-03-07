package com.kakaoinsurance.payment.application.port.in;

import com.kakaoinsurance.payment.domain.Member;

public interface GetMemberUseCase {

    Member getMember(GetMemberQuery getMemberQuery);

}
