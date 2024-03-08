package com.kakaoinsurance.payment.application.port.out;

import com.kakaoinsurance.payment.domain.Member;

/**
 * 회원 등록 용 out port
 */
public interface RegisterMemberOutPort {

    /**
     * 회원 등록
     *
     * @param member 등록할 회원 정보
     * @return 등록된 회원 정보
     */
    Member registerMember(Member member);

}
