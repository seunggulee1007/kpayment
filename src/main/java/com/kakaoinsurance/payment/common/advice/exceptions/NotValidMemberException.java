package com.kakaoinsurance.payment.common.advice.exceptions;

public class NotValidMemberException extends RuntimeException {

    public NotValidMemberException() {
        super("일치하는 계정이 존재하지 않습니다.");
    }

    public NotValidMemberException(String message) {
        super(message);
    }

}
