package com.kakaoinsurance.payment.application.port.out;

import com.kakaoinsurance.payment.domain.Payment;

public interface CancelPaymentOutPort {

    Payment cancelPayment(Payment payment);

}
