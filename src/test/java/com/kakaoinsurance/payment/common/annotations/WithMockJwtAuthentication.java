package com.kakaoinsurance.payment.common.annotations;

import com.kakaoinsurance.payment.common.security.WithMockJwtAuthenticationSecurityContextFactory;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockJwtAuthenticationSecurityContextFactory.class)
public @interface WithMockJwtAuthentication {

    long id() default 1L;

    String memberId() default "";

    String email() default "seunggu.lee@kakaopayinsurance.com";

    String nickname() default "카카오페이손해보험";

}