package com.kakaoinsurance.payment.adapter.out.persistence;

import java.security.SecureRandom;

/**
 * 관리번호 생성 Generator
 *
 * @author seunggu.lee
 */
public class PaymentIdGenerator {

    // starts at the year 2010
    private static final long EPOCH_MILLIS = 1262304000000L;
    // left shift amounts
    private static final int TIMESTAMP_SHIFT = 23;
    // exclusive
    private static final int MAX_RANDOM = 0x800000;

    private PaymentIdGenerator() {
    }

    public static String generate() {
        long time = System.currentTimeMillis() - EPOCH_MILLIS;
        return "P".concat(Long.toString((time << TIMESTAMP_SHIFT) + new SecureRandom().nextInt(MAX_RANDOM)));
    }

}