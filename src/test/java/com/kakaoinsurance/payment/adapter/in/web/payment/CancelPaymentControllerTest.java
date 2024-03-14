package com.kakaoinsurance.payment.adapter.in.web.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaoinsurance.payment.application.port.in.payment.GetPaymentUseCase;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
@DisplayName("결제 전체 취소 컨트롤러 테스트")
class CancelPaymentControllerTest extends RedisTestContainer {

    @Autowired
    private GetPaymentUseCase getPaymentUseCase;

    @Autowired
    private PaymentUseCase paymentUseCase;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Test
    @WithMockJwtAuthentication
    @DisplayName("주문 취소 테스트")
    void cancel_payment_success() throws Exception {
        // given & when
        Payment payment = saveDefaultPayment();
        // then
        assertThat(payment).isNotNull();
        String managementId = payment.paymentId().getId();
        CancelPaymentRequest request = new CancelPaymentRequest();
        request.setManagementId(managementId);
        request.setCancelPrice(payment.price());

        this.mockMvc.perform(delete("/api/payment").contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(
            request))).andDo(print()).andExpect(status().isOk());

    }

    @Test
    @WithMockJwtAuthentication
    @DisplayName("결제 전체 취소 실패(금액 틀림)")
    void cancel_payment_fail() throws Exception {
        // given & when
        Payment payment = saveDefaultPayment();
        // then
        assertThat(payment).isNotNull();
        String managementId = payment.paymentId().getId();
        CancelPaymentRequest request = new CancelPaymentRequest();
        request.setManagementId(managementId);
        request.setCancelPrice(500d);

        this.mockMvc.perform(delete("/api/payment").contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(
                request))).andDo(print()).andExpect(status().is4xxClientError())
            .andExpect(jsonPath("status").value(400))
            .andExpect(jsonPath("message").value("전체 취소일 경우 결제 금액과 취소 금액이 일치해야 합니다."))
        ;
        request.setCancelPrice(payment.price());
        request.setCancelTax(50L);
        this.mockMvc.perform(delete("/api/payment").contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(
                request))).andDo(print()).andExpect(status().is4xxClientError())
            .andExpect(jsonPath("status").value(400))
            .andExpect(jsonPath("message").value("취소 부가세가 유효하지 않습니다."))
        ;

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