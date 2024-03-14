package com.kakaoinsurance.payment.domain.payment;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record Payment(
    // 관리 번호
    PaymentId paymentId,
    // 카드 번호
    String cardNumber,
    // cvc
    String cvc,
    // 유효기간
    String validYmd,
    // 암호화된 카드정보
    String cardData,
    // 결제/취소 구분
    PaymentKind paymentKind,
    // 할부 개월수
    InstallmentMonth installmentMonth,
    // 금액
    Double price,
    // 부가가치세
    Long tax,
    // 남은 결제 금액
    Double remainPrice,
    // 남은 부가가치세
    Long remainTax,
    // 원거래 관리 번호
    String originalManagementId,
    // 결제 사용자 식별자
    String paidBy,
    // 결제 시각
    LocalDateTime paidAt

) {

}
