package com.kakaoinsurance.payment.application.port.out.member;

import com.kakaoinsurance.payment.domain.member.Member;

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
