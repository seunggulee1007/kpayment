package com.kakaoinsurance.payment.common.annotations;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

import static com.kakaoinsurance.payment.common.annotations.DataType.S;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface PayType {

    DataType dataType() default S;

    int length();

    boolean exclude() default false;

    int order();

}
