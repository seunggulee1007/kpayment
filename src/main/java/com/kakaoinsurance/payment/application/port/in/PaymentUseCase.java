package com.kakaoinsurance.payment.application.port.in;

import com.kakaoinsurance.payment.domain.Payment;

public interface PaymentUseCase {

    Payment payment(PaymentCommand command);

}
