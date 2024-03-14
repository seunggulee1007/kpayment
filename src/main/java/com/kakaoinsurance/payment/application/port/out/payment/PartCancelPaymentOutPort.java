package com.kakaoinsurance.payment.application.port.out.payment;

import com.kakaoinsurance.payment.domain.payment.Payment;

public interface PartCancelPaymentOutPort {

    Payment partCancelPayment(Payment payment);

}
