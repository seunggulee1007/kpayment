package com.kakaoinsurance.payment.adapter.in.web;

import com.kakaoinsurance.payment.application.port.in.RegisterMemberCommand;
import lombok.Getter;
import lombok.Setter;

import static org.springframework.beans.BeanUtils.copyProperties;

@Getter
@Setter
public class RegisterMemberRequest {

    private String nickname;
    private String email;
    private String password;
    private String confirmPassword;

    public RegisterMemberCommand convertCommand() {
        RegisterMemberCommand registerMemberCommand = new RegisterMemberCommand();
        copyProperties(this, registerMemberCommand);
        return registerMemberCommand;
    }

}
