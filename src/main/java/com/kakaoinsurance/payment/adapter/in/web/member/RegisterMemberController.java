package com.kakaoinsurance.payment.adapter.in.web.member;

import com.kakaoinsurance.payment.application.port.in.member.RegisterMemberUseCase;
import com.kakaoinsurance.payment.application.port.in.member.RegisteredMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원 등록 컨트롤러
 *
 * @author seunggu.lee
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member/auth")
public class RegisterMemberController {

    private final RegisterMemberUseCase registerMemberUseCase;

    @PostMapping
    public ResponseEntity<RegisteredMember> registerAccount(@RequestBody RegisterMemberRequest registerMemberRequest) {
        return ResponseEntity.ok(registerMemberUseCase.registerMember(registerMemberRequest.mapToCommand()));
    }

}
