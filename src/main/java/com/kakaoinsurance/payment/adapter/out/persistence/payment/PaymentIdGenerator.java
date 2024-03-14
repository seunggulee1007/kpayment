package com.kakaoinsurance.payment.adapter.out.persistence.payment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * 관리번호 생성 Generator
 *
 * @author seunggu.lee
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentIdGenerator {

    public static String generate() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid.substring(0, 20);

    }

}