package com.kakaoinsurance.payment.adapter.in.web.payment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("결제 전체 취소 request 테스트")
@ActiveProfiles("test")
class CancelPaymentRequestTest {

    @Test
    @DisplayName("취소 request 객체 생성 테스트")
    void create() {
        // given
        CancelPaymentRequest request = new CancelPaymentRequest();
        String managementId = "12345678910";
        double price = 100d;
        long tax = 10L;
        // when
        request.setManagementId(managementId);
        request.setCancelPrice(price);
        request.setCancelTax(tax);
        // then
        assertThat(request).isNotNull();
        assertThat(request.getManagementId()).isEqualTo(managementId);
        assertThat(request.getCancelPrice()).isEqualTo(price);
        assertThat(request.getCancelTax()).isEqualTo(tax);
    }

}