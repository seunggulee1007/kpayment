package com.kakaoinsurance.payment.application.port.out;

import com.kakaoinsurance.payment.domain.Payment;

public interface GetPaymentOutPort {

    Payment findById(String paymentId);

}
