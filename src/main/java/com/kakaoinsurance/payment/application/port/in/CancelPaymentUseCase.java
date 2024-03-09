package com.kakaoinsurance.payment.application.port.in;

import com.kakaoinsurance.payment.domain.Payment;

public interface CancelPaymentUseCase {

    Payment cancelPayment(CancelPaymentCommand command);

}
