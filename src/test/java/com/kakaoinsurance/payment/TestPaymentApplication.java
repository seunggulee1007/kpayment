package com.kakaoinsurance.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestPaymentApplication {

    public static void main(String[] args) {
        SpringApplication.from(PaymentApplication::main).with(TestPaymentApplication.class).run(args);
    }

}
