package com.kakaoinsurance.payment.common.advice.exceptions;

public class NotPresentPaymentException extends RuntimeException {

    public NotPresentPaymentException() {
        super("일치하는 결제 내역이 없습니다.");
    }

}
