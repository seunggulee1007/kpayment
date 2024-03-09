package com.kakaoinsurance.payment.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("결제 도메인 테스트")
class PaymentTest {

    @Test
    @DisplayName("Payment 생성 테스트")
    void create() {
        // given
        String paymentId = "1234";
        String cvc = "123";
        String cardNumber = "1234567";
        String validYmd = "1122";
        String cardData = "asdfasdfasd";
        PaymentKind paymentKind = PaymentKind.PAYMENT;
        InstallmentMonth installmentMonth = InstallmentMonth.ZERO;
        double price = 150000d;
        long tax = 1000L;
        String originalManagementId = "14512512512sdfa";
        LocalDateTime paidAt = LocalDateTime.now();
        String paidBy = "seunggu";
        // when
        Payment payment = Payment.builder()
            .paymentId(PaymentId.of(paymentId))
            .cvc(cvc)
            .cardNumber(cardNumber)
            .validYmd(validYmd)
            .cardData(cardData)
            .paymentKind(paymentKind)
            .installmentMonth(installmentMonth)
            .price(price)
            .tax(tax)
            .originalManagementId(originalManagementId)
            .paidBy(paidBy)
            .paidAt(paidAt)
            .build();

        // then
        assertThat(payment.paymentId().getId()).isEqualTo(paymentId);
        assertThat(payment.cvc()).isEqualTo(cvc);
        assertThat(payment.cardNumber()).isEqualTo(cardNumber);
        assertThat(payment.validYmd()).isEqualTo(validYmd);
        assertThat(payment.cardData()).isEqualTo(cardData);
        assertThat(payment.paymentKind()).isEqualTo(paymentKind);
        assertThat(payment.installmentMonth()).isEqualTo(installmentMonth);
        assertThat(payment.price()).isEqualTo(price);
        assertThat(payment.originalManagementId()).isEqualTo(originalManagementId);
        assertThat(payment.paidAt()).isEqualTo(paidAt);
        assertThat(payment.paidBy()).isEqualTo(paidBy);

    }

}