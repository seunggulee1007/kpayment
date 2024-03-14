package com.kakaoinsurance.payment.common.advice.exceptions;

public class AlreadyPaymentException extends RuntimeException {

    public AlreadyPaymentException() {
        super("이미 진행중인 결제 건이 존재합니다. 결제가 끝난 이후 재시도 해 주세요.");
    }

}
