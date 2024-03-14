package com.kakaoinsurance.payment.application.port.in.member;

import com.kakaoinsurance.payment.domain.member.Member;
import lombok.Data;

@Data
public class RegisteredMember {

    private String memberId;

    /**
     * 닉네임
     */
    private String nickname;

    /**
     * 이메일
     */
    private String email;

    public static RegisteredMember mapToDto(Member member) {
        RegisteredMember registeredMember = new RegisteredMember();
        registeredMember.memberId = member.memberId().getId();
        registeredMember.nickname = member.nickname();
        registeredMember.email = member.email();
        return registeredMember;
    }

}
