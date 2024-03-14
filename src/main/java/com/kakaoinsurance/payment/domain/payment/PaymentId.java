package com.kakaoinsurance.payment.domain.payment;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class PaymentId {

    private String id;

    public static PaymentId of(String id) {
        PaymentId paymentId = new PaymentId();
        paymentId.id = id;
        return paymentId;
    }

}
