package com.kakaoinsurance.payment.domain;

import com.kakaoinsurance.payment.common.annotations.DataType;
import com.kakaoinsurance.payment.common.annotations.PayType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record Payment(
    // 관리 번호
    @PayType(dataType = DataType.N, length = 4)
    PaymentId paymentId,
    // 카드 번호
    @PayType(dataType = DataType.L, length = 20)
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
    double price,
    // 부가가치세
    double tax,
    // 원거래 관리 번호
    String originalManagementId,
    // 결제 사용자 식별자
    String paidBy,
    // 결제 시각
    LocalDateTime paidAt

) {

}
