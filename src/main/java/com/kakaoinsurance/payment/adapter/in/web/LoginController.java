package com.kakaoinsurance.payment.adapter.in.web;

import com.kakaoinsurance.payment.application.port.in.LoginCommand;
import com.kakaoinsurance.payment.application.port.in.LoginUseCase;
import com.kakaoinsurance.payment.domain.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 사용자 로그인 컨트롤러
 * 로그인 후 JWT 토큰을 반환한다.
 *
 * @author seunggu.lee
 * @see JwtToken
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/member/auth/login")
public class LoginController {

    private final LoginUseCase loginUseCase;

    @PostMapping
    public JwtToken login(@RequestBody LoginRequest loginRequest) {
        return loginUseCase.login(LoginCommand.of(loginRequest.getEmail(), loginRequest.getPassword()));
    }

}
