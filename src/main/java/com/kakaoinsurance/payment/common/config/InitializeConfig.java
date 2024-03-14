package com.kakaoinsurance.payment.common.config;

import com.kakaoinsurance.payment.adapter.in.web.member.RegisterMemberRequest;
import com.kakaoinsurance.payment.application.port.in.member.LoginCommand;
import com.kakaoinsurance.payment.application.port.in.member.LoginUseCase;
import com.kakaoinsurance.payment.application.port.in.member.RegisterMemberUseCase;
import com.kakaoinsurance.payment.common.advice.exceptions.NotValidMemberException;
import com.kakaoinsurance.payment.common.properties.InitializeProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

/**
 * 어플리케이션이 실행시 최초로 실행되어야 하는 로직 수행하는 클래스
 * 사용자 로그인 시도 후, 실패한다면 사용자 새로 등록.
 * {@link com.kakaoinsurance.payment.application.port.in.member.GetMemberUseCase} 를 사용하지 않은 이유는 MemberId (Long) 으로 가져오는 메서드밖에 없기 때문.
 * 추후 추가된다면 해당 useCase 로 변경 예정
 */
@Configuration
@RequiredArgsConstructor
public class InitializeConfig {

    private final LoginUseCase loginUseCase;
    private final InitializeProperties initializeProperties;
    private final RegisterMemberUseCase registerMemberUseCase;

    @Bean
    @Transactional
    public ApplicationRunner applicationRunner() {
        return args -> {
            try {
                loginUseCase.login(LoginCommand.of(initializeProperties.getEmail(), initializeProperties.getPassword()));
            } catch (NotValidMemberException e) {
                RegisterMemberRequest request = new RegisterMemberRequest();
                request.setNickname(initializeProperties.getNickname());
                request.setEmail(initializeProperties.getEmail());
                request.setPassword(initializeProperties.getPassword());
                request.setConfirmPassword(initializeProperties.getPassword());
                registerMemberUseCase.registerMember(request.mapToCommand());
            }
        };
    }

}
