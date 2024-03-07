package com.kakaoinsurance.payment.application.port.in;

import com.kakaoinsurance.payment.common.SelfValidating;
import com.kakaoinsurance.payment.common.advice.exceptions.NotMatchedPasswordException;
import com.kakaoinsurance.payment.domain.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;

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
        this.validateSelf();
        if (!password.equals(confirmPassword)) {
            throw new NotMatchedPasswordException("비밀번호가 일치하지 않습니다.");
        }
        return Member.builder().nickname(this.nickname)
            .password(this.password)
            .email(this.email)
            .build();
    }

}
