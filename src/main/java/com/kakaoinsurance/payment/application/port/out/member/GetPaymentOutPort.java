package com.kakaoinsurance.payment.application.port.out.member;

import com.kakaoinsurance.payment.domain.payment.Payment;

public interface GetPaymentOutPort {

    Payment findById(String paymentId);

}
