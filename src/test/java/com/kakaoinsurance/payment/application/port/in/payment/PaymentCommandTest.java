package com.kakaoinsurance.payment.application.port.in.payment;

import com.kakaoinsurance.payment.adapter.in.web.payment.PaymentRequest;
import com.kakaoinsurance.payment.common.advice.exceptions.PaymentBadRequestException;
import com.kakaoinsurance.payment.domain.payment.InstallmentMonth;
import jakarta.validation.ConstraintViolationException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static com.kakaoinsurance.payment.common.DefaultMessage.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@DisplayName("결제 command 생성")
class PaymentCommandTest {

    @Test
    @DisplayName("필수값 누락 테스트")
    void required_test() {
        // given
        PaymentRequest request = new PaymentRequest();

        // when
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, request::mapToCommand);
        // then
        assertThat(exception.getMessage()).contains("카드");
        // given
        request.setCardNumber("12345678910");
        // when
        exception = assertThrows(ConstraintViolationException.class, request::mapToCommand);
        // then
        assertThat(exception.getMessage()).contains("유효기간");
        // given
        request.setValidYmd("1223");
        // when
        exception = assertThrows(ConstraintViolationException.class, request::mapToCommand);
        // then
        assertThat(exception.getMessage()).contains("cvc");
        // given
        request.setCvc("777");
        // when
        exception = assertThrows(ConstraintViolationException.class, request::mapToCommand);
        // then
        assertThat(exception.getMessage()).contains("할부 개월수");
        // given
        request.setInstallmentMonth(InstallmentMonth.ZERO);
        // when
        exception = assertThrows(ConstraintViolationException.class, request::mapToCommand);
        // then
        assertThat(exception.getMessage()).contains("결제금액");
    }

    @Test
    @DisplayName("카드 넘버 자리수 오류 체크")
    void validate_cardNumber() {
        // given
        PaymentRequest request = new PaymentRequest();
        String cardNumber = "12345678";
        request.setCardNumber(cardNumber);
        // when
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, request::mapToCommand);
        // then
        assertThat(exception.getMessage()).contains("카드 번호는 10 ~ 16자리만 가능합니다.");
        // given
        cardNumber = "12341234123412341234";
        request.setCardNumber(cardNumber);
        // when
        exception = assertThrows(ConstraintViolationException.class, request::mapToCommand);
        // then
        assertThat(exception.getMessage()).contains("카드 번호는 10 ~ 16자리만 가능합니다.");

    }

    @Test
    @DisplayName("결제 금액 검증")
    void payment_validate_price() {
        // given
        PaymentRequest request = getDefaultRequest();
        // given
        request.setPrice(10d);
        // when
        PaymentBadRequestException exception = assertThrows(PaymentBadRequestException.class, request::mapToCommand);
        // then
        assertThat(exception.getMessage()).isEqualTo(PAYMENT_RANGE.getMessage());

        // given
        request.setPrice(10000000000d);
        // when
        exception = assertThrows(PaymentBadRequestException.class, request::mapToCommand);

        // then
        assertThat(exception.getMessage()).isEqualTo(PAYMENT_RANGE.getMessage());

        // given
        request.setPrice(1000d);
        request.setTax(1001L);
        // when
        exception = assertThrows(PaymentBadRequestException.class, request::mapToCommand);

        // then
        assertThat(exception.getMessage()).isEqualTo(CANCEL_TAX_LATHER_THEN_PRICE.getMessage());
    }

    @Test
    @DisplayName("유효기간 검증")
    void payment_valid_ymd() {
        // given
        PaymentRequest request = getDefaultRequest();
        request.setValidYmd("2512");
        // when
        PaymentBadRequestException exception = assertThrows(PaymentBadRequestException.class, request::mapToCommand);
        // then
        assertThat(exception.getMessage()).isEqualTo(NOT_VALID_YMD.getMessage());

        // given
        request.setValidYmd("1223");
        // when
        exception = assertThrows(PaymentBadRequestException.class, request::mapToCommand);
        // then
        assertThat(exception.getMessage()).isEqualTo(NOT_VALID_YMD.getMessage());

        // given
        request.setValidYmd("123");
        // when
        exception = assertThrows(PaymentBadRequestException.class, request::mapToCommand);
        // then
        assertThat(exception.getMessage()).isEqualTo(NOT_VALID_YMD.getMessage());
    }

    @NotNull
    private static PaymentRequest getDefaultRequest() {
        PaymentRequest request = new PaymentRequest();
        request.setCardNumber("12345678910");
        request.setValidYmd("1226");
        request.setCvc("777");
        request.setInstallmentMonth(InstallmentMonth.ZERO);
        request.setPrice(1000d);
        request.setTax(100L);
        return request;
    }

}