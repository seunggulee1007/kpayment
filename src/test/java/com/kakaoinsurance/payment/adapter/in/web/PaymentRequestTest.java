package com.kakaoinsurance.payment.adapter.in.web;

import com.kakaoinsurance.payment.domain.InstallmentMonth;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentRequestTest {

    @Test
    @DisplayName("요청부 생성 테스트")
    void create() {
        // given
        String cvc = "123";
        String cardNumber = "1234567";
        String validYmd = "1122";
        InstallmentMonth installmentMonth = InstallmentMonth.ZERO;
        double price = 150000d;
        long tax = 1000L;
        // when
        PaymentRequest request = new PaymentRequest();
        request.setCardNumber(cardNumber);
        request.setCvc(cvc);
        request.setValidYmd(validYmd);
        request.setInstallmentMonth(installmentMonth);
        request.setPrice(price);
        request.setTax(tax);

        // then
        assertThat(request).isNotNull();
        assertThat(request.getCardNumber()).isEqualTo(cardNumber);
        assertThat(request.getCvc()).isEqualTo(cvc);
        assertThat(request.getValidYmd()).isEqualTo(validYmd);
        assertThat(request.getPrice()).isEqualTo(price);
        assertThat(request.getTax()).isEqualTo(tax);
    }

}