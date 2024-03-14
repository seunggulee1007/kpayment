package com.kakaoinsurance.payment.adapter.in.web.payment;

import com.kakaoinsurance.payment.application.port.in.payment.PaymentCommand;
import com.kakaoinsurance.payment.application.port.in.payment.PaymentUseCase;
import com.kakaoinsurance.payment.common.annotations.MockMvcTest;
import com.kakaoinsurance.payment.common.annotations.WithMockJwtAuthentication;
import com.kakaoinsurance.payment.common.container.RedisTestContainer;
import com.kakaoinsurance.payment.domain.payment.InstallmentMonth;
import com.kakaoinsurance.payment.domain.payment.Payment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
class GetPaymentControllerTest extends RedisTestContainer {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PaymentUseCase paymentUseCase;

    @Test
    @DisplayName("조회 테스트")
    @WithMockJwtAuthentication
    void getPayment() throws Exception {
        // given
        Payment payment = saveDefaultPayment();
        String managementId = payment.paymentId().getId();

        // when
        this.mockMvc.perform(get("/api/payment/".concat(managementId)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("success").value(true));

        // then

    }

    private Payment saveDefaultPayment() {
        PaymentCommand command = new PaymentCommand();
        command.setPrice(110000d);
        command.setTax(10000L);
        command.setCardNumber("1234567890123456");
        command.setCvc("777");
        command.setInstallmentMonth(InstallmentMonth.ZERO);
        command.setValidYmd("1125");
        return paymentUseCase.payment(command);
    }

}