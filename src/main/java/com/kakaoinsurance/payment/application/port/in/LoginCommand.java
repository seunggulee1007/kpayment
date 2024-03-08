package com.kakaoinsurance.payment.application.port.in;

import com.kakaoinsurance.payment.common.SelfValidating;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginCommand extends SelfValidating<LoginCommand> {

    /**
     * 이메일
     */
    @NotNull(message = "이메일은 필수입니다.")
    private String email;

    /**
     * 비밀번호
     */
    @NotNull(message = "비밀번호를 입력해 주세요.")
    private String password;

    public static LoginCommand of(String email, String password) {
        LoginCommand loginCommand = new LoginCommand();
        loginCommand.email = email;
        loginCommand.password = password;
        loginCommand.validateSelf();
        return loginCommand;
    }

}
