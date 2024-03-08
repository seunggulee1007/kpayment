package com.kakaoinsurance.payment.common.advice.exceptions;

/**
 * 결제 중 발생한 에외 처리
 *
 * @author seunggu.lee
 */
public class PaymentBadRequestException extends RuntimeException {

    public PaymentBadRequestException() {
        super();
    }

    public PaymentBadRequestException(String message) {
        super(message);
    }

}
