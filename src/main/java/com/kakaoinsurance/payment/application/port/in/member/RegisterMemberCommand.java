package com.kakaoinsurance.payment.application.port.in.member;

import com.kakaoinsurance.payment.common.SelfValidating;
import com.kakaoinsurance.payment.common.advice.exceptions.NotMatchedPasswordException;
import com.kakaoinsurance.payment.domain.member.Member;
import com.kakaoinsurance.payment.domain.member.MemberRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;

import java.util.Set;

@Setter
public class RegisterMemberCommand extends SelfValidating<RegisterMemberCommand> {

    @NotNull
    private String nickname;
    @Email
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String confirmPassword;

    public Member toDomain() {
        if (!password.equals(confirmPassword)) {
            throw new NotMatchedPasswordException("비밀번호가 일치하지 않습니다.");
        }
        return Member.builder()
            .nickname(this.nickname)
            .password(this.password)
            .email(this.email)
            .roles(Set.of(MemberRole.USER))
            .build();
    }

}
