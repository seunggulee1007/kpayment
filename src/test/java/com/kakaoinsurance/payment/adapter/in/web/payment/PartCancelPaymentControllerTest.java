package com.kakaoinsurance.payment.adapter.in.web.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static com.kakaoinsurance.payment.common.DefaultMessage.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
@DisplayName("부분 취소 Controller 테스트")
class PartCancelPaymentControllerTest extends RedisTestContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PaymentUseCase paymentUseCase;

    @Test
    @WithMockJwtAuthentication
    @DisplayName("결제 부분 취소 테스트 케이스 1")
    void test_case_1() throws Exception {
        // given
        Payment payment = saveDefaultPayment(11000d, 1000L);

        PartCancelPaymentRequest request = new PartCancelPaymentRequest();
        request.setManagementId(payment.paymentId().getId());
        request.setCancelPrice(1100d);
        request.setCancelTax(100L);

        // 부분 취소 1,100 / 100 -> 성공
        this.mockMvc.perform(put("/api/payment").content(this.objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("success").value(true))
        ;
        request.setCancelPrice(3300d);
        request.setCancelTax(null);
        // 부분 취소 성공 3,300 / null
        this.mockMvc.perform(put("/api/payment").content(this.objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("success").value(true))
        ;

        request.setCancelPrice(7700d);
        request.setCancelTax(null);
        // 부분 취소 실패 7,700 / null
        this.mockMvc.perform(put("/api/payment").content(this.objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("success").value(false))
            .andExpect(jsonPath("message").value(CANCEL_PRICE_LATHER_THEN_PRICE.getMessage()))
        ;

        request.setCancelPrice(6600d);
        request.setCancelTax(700L);
        // 부분 취소 실패 6,600 / 700
        this.mockMvc.perform(put("/api/payment").content(this.objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("success").value(false))
            .andExpect(jsonPath("message").value(NOT_VALID_TAX.getMessage()))
        ;

        request.setCancelPrice(6600d);
        request.setCancelTax(600L);
        // 부분 취소 성공 6,600 / 600
        this.mockMvc.perform(put("/api/payment").content(this.objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("success").value(true))
        ;

        request.setCancelPrice(100d);
        request.setCancelTax(null);
        // 부분 취소 실패 100 / null
        this.mockMvc.perform(put("/api/payment").content(this.objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("success").value(false))
            .andExpect(jsonPath("message").value(COMPLETED_CANCEL.getMessage()))
        ;

    }

    @Test
    @WithMockJwtAuthentication
    @DisplayName("결제 부분 취소 테스트 케이스 2")
    void test_case_2() throws Exception {
        Payment payment = saveDefaultPayment(20000d, 909L);
        PartCancelPaymentRequest request = new PartCancelPaymentRequest();
        request.setManagementId(payment.paymentId().getId());
        request.setCancelPrice(10000d);
        request.setCancelTax(0L);

        // 부분 취소 10,000 / 0 -> 성공
        this.mockMvc.perform(put("/api/payment").content(this.objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("success").value(true))
        ;

        request.setCancelPrice(10000d);
        request.setCancelTax(0L);

        // 부분 취소 10,000 / 0 -> 실패
        this.mockMvc.perform(put("/api/payment").content(this.objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("success").value(false))
            .andExpect(jsonPath("message").value(NOT_VALID_TAX.getMessage()))
        ;

        request.setCancelPrice(10000d);
        request.setCancelTax(909L);

        // 부분 취소 10,000 / 909 -> 성공
        this.mockMvc.perform(put("/api/payment").content(this.objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("success").value(true))
        ;
    }

    @Test
    @WithMockJwtAuthentication
    @DisplayName("결제 부분 취소 테스트 케이스 3")
    void test_case_3() throws Exception {
        Payment payment = saveDefaultPayment(20000d, null);
        PartCancelPaymentRequest request = new PartCancelPaymentRequest();
        request.setManagementId(payment.paymentId().getId());
        request.setCancelPrice(10000d);
        request.setCancelTax(1000L);

        // 부분 취소 10,000 / 1000 -> 성공
        this.mockMvc.perform(put("/api/payment").content(this.objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("success").value(true))
        ;

        request.setCancelPrice(10000d);
        request.setCancelTax(909L);

        // 부분 취소 10,000 / 909 -> 실패
        this.mockMvc.perform(put("/api/payment").content(this.objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("success").value(false))
            .andExpect(jsonPath("message").value(NOT_VALID_TAX.getMessage()))
        ;

        request.setCancelPrice(10000d);
        request.setCancelTax(null);

        // 부분 취소 10,000 / null -> 성공
        this.mockMvc.perform(put("/api/payment").content(this.objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("success").value(true))
        ;

    }

    private Payment saveDefaultPayment(double price, Long tax) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setPrice(price);
        paymentRequest.setTax(tax);
        paymentRequest.setCardNumber("12345678910");
        paymentRequest.setCvc("777");
        paymentRequest.setValidYmd("1226");
        paymentRequest.setInstallmentMonth(InstallmentMonth.ONE);
        return paymentUseCase.payment(paymentRequest.mapToCommand());
    }

}