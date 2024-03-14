package com.kakaoinsurance.payment.application.port.in.payment;

import com.kakaoinsurance.payment.adapter.in.web.payment.CancelPaymentRequest;
import com.kakaoinsurance.payment.common.advice.exceptions.PaymentBadRequestException;
import com.kakaoinsurance.payment.common.container.RedisTestContainer;
import com.kakaoinsurance.payment.domain.payment.InstallmentMonth;
import com.kakaoinsurance.payment.domain.payment.Payment;
import com.kakaoinsurance.payment.domain.payment.PaymentKind;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("결제 취소 UseCase 테스트")
@ActiveProfiles("test")
@SpringBootTest
class CancelPaymentUseCaseTest extends RedisTestContainer {

    @Autowired
    private PaymentUseCase paymentUseCase;

    @Autowired
    private CancelPaymentUseCase cancelPaymentUseCase;

    @Test
    @DisplayName("결제 취소 성공")
    void cancelPayment_success() {
        // given
        String managementId = saveDefaultPayment();
        double price = 110000d;

        CancelPaymentRequest request = getCancelPaymentRequest(managementId, price);
        // when
        Payment payment = cancelPaymentUseCase.cancelPayment(request.mapToCommand());
        // then
        assertThat(payment.price()).isEqualTo(price);
        assertThat(payment.paymentKind()).isEqualTo(PaymentKind.CANCEL);

    }

    @Test
    @DisplayName("전체 결제 취소 - 취소 금액 다름")
    void cancelPayment_fail() {
        // given
        String managementId = saveDefaultPayment();
        double price = 5500d;
        // when
        CancelPaymentRequest request = getCancelPaymentRequest(managementId, price);
        CancelPaymentCommand command = request.mapToCommand();
        // then
        PaymentBadRequestException exception = assertThrows(PaymentBadRequestException.class, () -> {
            cancelPaymentUseCase.cancelPayment(command);
        });
        assertThat(exception.getMessage()).isEqualTo("전체 취소일 경우 결제 금액과 취소 금액이 일치해야 합니다.");
    }

    @Test
    @DisplayName("전체 결제 취소 요청 재전송 실패")
    void cancel_fail_request_twice() {
        // given
        String managementId = saveDefaultPayment();
        double price = 110000d;

        CancelPaymentRequest request = getCancelPaymentRequest(managementId, price);
        // when
        cancelPaymentUseCase.cancelPayment(request.mapToCommand());

        // then
        CancelPaymentCommand command = request.mapToCommand();
        PaymentBadRequestException exception = assertThrows(PaymentBadRequestException.class, () -> {
            cancelPaymentUseCase.cancelPayment(command);
        });

        assertThat(exception.getMessage()).isEqualTo("결제에 대한 전체취소는 1번만 가능합니다.");
    }

    @Test
    @DisplayName("부가세 요청 실패")
    void cancel_fail_tax() {
        // given
        String managementId = saveDefaultPayment();
        double price = 110000d;
        long tax = 2000L;
        CancelPaymentRequest request = getCancelPaymentRequest(managementId, price);
        // when
        request.setCancelTax(tax);
        CancelPaymentCommand command = request.mapToCommand();
        PaymentBadRequestException exception = assertThrows(PaymentBadRequestException.class, () -> {
            cancelPaymentUseCase.cancelPayment(command);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo("취소 부가세가 유효하지 않습니다.");

    }

    @NotNull
    private static CancelPaymentRequest getCancelPaymentRequest(String managementId, double price) {
        CancelPaymentRequest request = new CancelPaymentRequest();
        request.setManagementId(managementId);
        request.setCancelPrice(price);
        return request;
    }

    private String saveDefaultPayment() {
        PaymentCommand command = new PaymentCommand();
        String cardNumber = "1234567890123456";
        String cvc = "777";
        String validYmd = "1125";
        InstallmentMonth installmentMonth = InstallmentMonth.ZERO;
        double price = 110000d;
        long tax = 10000L;
        command.setCardNumber(cardNumber);
        command.setCvc(cvc);
        command.setValidYmd(validYmd);
        command.setInstallmentMonth(installmentMonth);
        command.setPrice(price);
        command.setTax(tax);
        Payment payment = paymentUseCase.payment(command);
        return payment.paymentId().getId();

    }

}