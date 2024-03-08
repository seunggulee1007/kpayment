package com.kakaoinsurance.payment.domain;

import lombok.Getter;

@Getter
public class PaymentId {

    private String id;

    public static PaymentId of(String id) {
        PaymentId paymentId = new PaymentId();
        paymentId.id = id;
        return paymentId;
    }

}
