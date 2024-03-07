package com.kakaoinsurance.payment.application.port.out;

import com.kakaoinsurance.payment.domain.Member;

public interface GetMemberPort {

    Member getMemberByEmailAndPassword(String email, String password);

    Member getMemberByIdAndPassword(Long memberId, String password);

}
