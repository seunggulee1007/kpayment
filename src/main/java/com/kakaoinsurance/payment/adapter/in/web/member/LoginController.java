package com.kakaoinsurance.payment.adapter.in.web.member;

import com.kakaoinsurance.payment.application.port.in.member.LoginCommand;
import com.kakaoinsurance.payment.application.port.in.member.LoginUseCase;
import com.kakaoinsurance.payment.common.utils.ApiUtil;
import com.kakaoinsurance.payment.domain.member.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kakaoinsurance.payment.common.utils.ApiUtil.success;

/**
 * 사용자 로그인 컨트롤러
 * 로그인 후 JWT 토큰을 반환한다.
 *
 * @author seunggu.lee
 * @see JwtToken
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member/auth/login")
public class LoginController {

    private final LoginUseCase loginUseCase;

    @PostMapping
    public ApiUtil.ApiResult<JwtToken> login(@RequestBody LoginRequest loginRequest) {
        return success(loginUseCase.login(LoginCommand.of(loginRequest.getEmail(), loginRequest.getPassword())));
    }

}
