package com.kakaoinsurance.payment.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DefaultMessage {

    NOT_VALID_TAX("취소 부가세가 유효하지 않습니다."),
    ALL_CANCEL_AVAILABLE("결제에 대한 전체취소는 1번만 가능합니다."),
    NOT_MATCHED_CANCEL_PRICE("전체 취소일 경우 결제 금액과 취소 금액이 일치해야 합니다."),
    CANCEL_PRICE_LATHER_THEN_PRICE("취소금액이 남은 금액보다 클 수 없습니다."),
    CANCEL_TAX_LATHER_THEN_PRICE("부가 가치세가 결제금액보다 클 수 없습니다."),
    COMPLETED_CANCEL("이미 취소가 완료된 건입니다."),
    NOT_VALID_YMD("유효기간이 바람직하지 않습니다."),
    PAYMENT_RANGE("결제금액은 100원 이상 10억원 이하 입니다."),
    ;

    private final String message;

}
