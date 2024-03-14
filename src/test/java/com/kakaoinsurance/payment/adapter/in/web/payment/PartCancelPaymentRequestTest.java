package com.kakaoinsurance.payment.adapter.in.web.payment;

import com.kakaoinsurance.payment.application.port.in.payment.PartCancelPaymentCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DisplayName("부분취소 요청 객체 테스트")
class PartCancelPaymentRequestTest {

    @Test
    @DisplayName("부분취소 요청 객체 생성 테스트")
    void create() {
        // given
        PartCancelPaymentRequest request = new PartCancelPaymentRequest();
        double cancelPrice = 1111d;
        long cancelTax = 100L;
        String managementId = "!23123123123";
        // when
        request.setManagementId(managementId);
        request.setCancelTax(cancelTax);
        request.setCancelPrice(cancelPrice);
        PartCancelPaymentCommand command = request.mapToCommand();
        // then
        assertThat(request).isNotNull();
        assertThat(request.getManagementId()).isEqualTo(managementId);
        assertThat(request.getCancelTax()).isEqualTo(cancelTax);
        assertThat(request.getCancelPrice()).isEqualTo(cancelPrice);
        assertThat(command.getManagementId()).isEqualTo(managementId);
        assertThat(command.getCancelTax()).isEqualTo(cancelTax);
        assertThat(command.getCancelPrice()).isEqualTo(cancelPrice);
    }

}