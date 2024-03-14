package com.kakaoinsurance.payment.application.port.out.member;

import com.kakaoinsurance.payment.domain.member.Member;

public interface GetMemberPort {

    /**
     * 이메일과 비밀번호가 일치하는 회원 조회
     * 로그인 시 이메일로 로그인 하기 때문에 해당 메서드를 사용한다.
     *
     * @param email    이메일
     * @param password 비밀번호
     * @return 조회된 회원
     */
    Member getMemberByEmailAndPassword(String email, String password);

    /**
     * 회원 식별자와 비밀번호가 일치하는 회원 조회
     * 시큐리티에서 인증시 사용하기 때문에 인덱스인 회원 아이디로 조회 ( 인증 때문에 빈번하게 조회 됨으로 인덱스 사용 )
     *
     * @param memberId 회원 식별자
     * @param password 비밀번호
     * @return 조회된 회원
     */
    Member getMemberByIdAndPassword(Long memberId, String password);

}
