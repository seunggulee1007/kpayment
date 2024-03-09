package com.kakaoinsurance.payment.application.port.in;

import com.kakaoinsurance.payment.common.advice.exceptions.PaymentBadRequestException;
import com.kakaoinsurance.payment.common.properties.StringProperties;
import com.kakaoinsurance.payment.domain.InstallmentMonth;
import com.kakaoinsurance.payment.domain.Payment;
import jakarta.validation.ConstraintViolationException;
import org.jasypt.encryption.StringEncryptor;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.StringTokenizer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PaymentUseCaseTest {

    @Autowired
    private PaymentUseCase paymentUseCase;

    @Autowired
    private StringEncryptor jasyptStringEncryptor;

    @Autowired
    private StringProperties stringProperties;

    @Test
    @DisplayName("결재 성공")
    void payment_success() {
        // given
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
        // when
        String separator = stringProperties.getCardEncryptSeparator();
        Payment payment = paymentUseCase.payment(command);
        String cardData = payment.cardData();
        String decryptedCardData = jasyptStringEncryptor.decrypt(cardData);
        String beforeCardData = cardNumber.concat(separator).concat(validYmd).concat(separator).concat(cvc);
        StringTokenizer tokenizer = new StringTokenizer(decryptedCardData, separator);

        // then
        assertThat(decryptedCardData).contains(separator);
        assertThat(beforeCardData).isEqualTo(decryptedCardData);
        assertThat(tokenizer.nextToken()).isEqualTo(cardNumber);
        assertThat(tokenizer.nextToken()).isEqualTo(validYmd);
        assertThat(tokenizer.nextToken()).isEqualTo(cvc);
        assertThat(price).isEqualTo(payment.price());
        assertThat(payment.paymentId()).isNotNull();
        assertThat(tax).isEqualTo(payment.tax());
        assertThat(installmentMonth).isEqualTo(payment.installmentMonth());

    }

    @Test
    @DisplayName("결제시 필수 값 누락 테스트")
    void payment_required_fail() {
        PaymentCommand command = new PaymentCommand();
        // 카드번호 필수 값 누락
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            paymentUseCase.payment(command);
        });
        assertThat(exception.getMessage()).contains("카드");
        command.setCardNumber("12345678910");
        // 유효기간 누락
        exception = assertThrows(ConstraintViolationException.class, () -> {
            paymentUseCase.payment(command);
        });
        assertThat(exception.getMessage()).contains("유효기간");
        command.setValidYmd("1223");
        // cvc 누락
        exception = assertThrows(ConstraintViolationException.class, () -> {
            paymentUseCase.payment(command);
        });
        assertThat(exception.getMessage()).contains("cvc");
        command.setCvc("777");
        exception = assertThrows(ConstraintViolationException.class, () -> {
            paymentUseCase.payment(command);
        });
        assertThat(exception.getMessage()).contains("할부 개월수");
        command.setInstallmentMonth(InstallmentMonth.ZERO);
        exception = assertThrows(ConstraintViolationException.class, () -> {
            paymentUseCase.payment(command);
        });
        assertThat(exception.getMessage()).contains("결제금액");
    }

    @Test
    @DisplayName("카드 넘버 자리수 오류 체크")
    void payment_fail_validate_cardNumber() {
        // given
        PaymentCommand command = new PaymentCommand();
        String cardNumber = "12345678";
        command.setCardNumber(cardNumber);
        // when
        assertThrows(ConstraintViolationException.class, () -> {
            paymentUseCase.payment(command);
        });

        // given
        cardNumber = "12341234123412341234";
        command.setCardNumber(cardNumber);
        // when
        assertThrows(ConstraintViolationException.class, () -> {
            paymentUseCase.payment(command);
        });
    }

    @Test
    @DisplayName("결제 금액 검증")
    void payment_validate_price() {
        // given
        PaymentCommand command = getDefaultCommand();

        // given
        command.setPrice(10d);
        // when
        PaymentBadRequestException exception = assertThrows(PaymentBadRequestException.class, () -> {
            paymentUseCase.payment(command);
        });
        // then
        assertThat(exception.getMessage()).isEqualTo("결제금액은 100원 이상 10억원 이하 입니다.");

        // given
        command.setPrice(10000000000d);
        // when
        exception = assertThrows(PaymentBadRequestException.class, () -> {
            paymentUseCase.payment(command);
        });
        // then
        assertThat(exception.getMessage()).isEqualTo("결제금액은 100원 이상 10억원 이하 입니다.");

        // given
        command.setPrice(1000d);
        command.setTax(1001L);
        // when
        exception = assertThrows(PaymentBadRequestException.class, () -> {
            paymentUseCase.payment(command);
        });
        // then
        assertThat(exception.getMessage()).isEqualTo("부가 가치세가 결제금액보다 클 수 없습니다.");
    }

    @Test
    @DisplayName("유효기간 검증")
    void payment_valid_ymd() {
        // given
        PaymentCommand command = getDefaultCommand();
        command.setValidYmd("2512");
        // when
        PaymentBadRequestException exception = assertThrows(PaymentBadRequestException.class, () -> {
            paymentUseCase.payment(command);
        });
        // then
        assertThat(exception.getMessage()).isEqualTo("유효기간이 바람직하지 않습니다.");

        // given
        command.setValidYmd("1223");
        // when
        exception = assertThrows(PaymentBadRequestException.class, () -> {
            paymentUseCase.payment(command);
        });
        // then
        assertThat(exception.getMessage()).isEqualTo("유효기간이 바람직하지 않습니다.");

        // given
        command.setValidYmd("123");
        // when
        exception = assertThrows(PaymentBadRequestException.class, () -> {
            paymentUseCase.payment(command);
        });
        // then
        assertThat(exception.getMessage()).isEqualTo("유효기간이 바람직하지 않습니다.");
    }

    @NotNull
    private static PaymentCommand getDefaultCommand() {
        PaymentCommand command = new PaymentCommand();
        command.setCardNumber("12345678910");
        command.setValidYmd("1226");
        command.setCvc("777");
        command.setInstallmentMonth(InstallmentMonth.ZERO);
        command.setPrice(1000d);
        command.setTax(100L);
        return command;
    }

}