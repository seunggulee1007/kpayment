package com.kakaoinsurance.payment.common.advice.exceptions;

public class NotValidCardDataException extends RuntimeException {

    public NotValidCardDataException() {
        super("유효한 카드 데이터가 아닙니다.");
    }

}
