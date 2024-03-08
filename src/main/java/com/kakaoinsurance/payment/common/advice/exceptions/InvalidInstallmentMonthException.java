package com.kakaoinsurance.payment.common.advice.exceptions;

public class InvalidInstallmentMonthException extends RuntimeException {

    public InvalidInstallmentMonthException() {
        super();
    }

    public InvalidInstallmentMonthException(String message) {
        super(message);
    }

}
