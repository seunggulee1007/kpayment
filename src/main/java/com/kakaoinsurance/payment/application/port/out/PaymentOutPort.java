package com.kakaoinsurance.payment.application.port.out;

import com.kakaoinsurance.payment.domain.Payment;

public interface PaymentOutPort {

    Payment savePayment(Payment payment);

}
