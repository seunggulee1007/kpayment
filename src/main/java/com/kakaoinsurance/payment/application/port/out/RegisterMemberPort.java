package com.kakaoinsurance.payment.application.port.out;

import com.kakaoinsurance.payment.domain.Member;

public interface RegisterMemberPort {

    Member registerMember(Member member);

}
