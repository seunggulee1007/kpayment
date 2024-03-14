package com.kakaoinsurance.payment.application.port.in.payment;

import com.kakaoinsurance.payment.domain.payment.Payment;

public interface PartCancelPaymentUseCase {

    Payment partCancelPayment(PartCancelPaymentCommand command);

}
