package com.kakaoinsurance.payment.adapter.in.web.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaoinsurance.payment.common.annotations.MockMvcTest;
import com.kakaoinsurance.payment.common.annotations.WithMockJwtAuthentication;
import com.kakaoinsurance.payment.common.container.RedisTestContainer;
import com.kakaoinsurance.payment.domain.payment.InstallmentMonth;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
@ActiveProfiles("test")
@DisplayName("결제 Controller 테스트")
class PaymentControllerTest extends RedisTestContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockJwtAuthentication
    @DisplayName("결제 성공")
    void payment_success() throws Exception {
        PaymentRequest request = new PaymentRequest();
        request.setCardNumber("1234567890123456");
        request.setCvc("777");
        request.setValidYmd("1129");
        request.setInstallmentMonth(InstallmentMonth.ZERO);
        request.setPrice(110000d);
        request.setTax(10000L);
        this.mockMvc.perform(post("/api/payment").contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(
                request)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("success").value(true))
        ;
    }

    @Test
    @WithMockJwtAuthentication
    @DisplayName("결제 실패")
    void payment_fail() throws Exception {
        PaymentRequest request = new PaymentRequest();
        request.setCardNumber("1234567890123456");
        request.setCvc("777");
        request.setValidYmd("1129");
        request.setInstallmentMonth(InstallmentMonth.ZERO);
        request.setPrice(100d);
        request.setTax(10000L);
        this.mockMvc.perform(post("/api/payment").contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(
                request)))
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("success").value(false))
            .andExpect(jsonPath("message").value("부가 가치세가 결제금액보다 클 수 없습니다."))
        ;
    }

}