package com.kakaoinsurance.payment.application.port.in.payment;

import com.kakaoinsurance.payment.common.advice.exceptions.PaymentBadRequestException;
import com.kakaoinsurance.payment.common.container.RedisTestContainer;
import com.kakaoinsurance.payment.domain.payment.InstallmentMonth;
import com.kakaoinsurance.payment.domain.payment.Payment;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("결제 부분 취소 UseCase 테스트")
@Transactional
class PartCancelPaymentUseCaseTest extends RedisTestContainer {

    @Autowired
    private PaymentUseCase paymentUseCase;
    @Autowired
    private PartCancelPaymentUseCase partCancelPaymentUseCase;

    @Test
    @DisplayName("테스트 케이스 1")
    void test_case_1() {
        // given
        Payment payment = saveDefaultPayment(11000d, 1000L);
        String managementId = payment.paymentId().getId();
        double price = 1100d;
        Long tax = 100L;
        PartCancelPaymentCommand command = getPartCancelPaymentCommand(managementId, price, tax);
        // when
        Payment partCancelPayment = partCancelPaymentUseCase.partCancelPayment(command);
        // then
        assertThat(partCancelPayment.originalManagementId()).isEqualTo(managementId);
        assertThat(partCancelPayment.price()).isEqualTo(price);
        assertThat(partCancelPayment.tax()).isEqualTo(tax);
        // given
        price = 3300;
        tax = null;
        command = getPartCancelPaymentCommand(managementId, price, tax);
        // when
        partCancelPayment = partCancelPaymentUseCase.partCancelPayment(command);
        // then
        assertThat(partCancelPayment.originalManagementId()).isEqualTo(managementId);
        assertThat(partCancelPayment.price()).isEqualTo(price);

        price = 7000;
        PartCancelPaymentCommand partCancelPaymentCommand = getPartCancelPaymentCommand(managementId, price, tax);
        // when
        assertThrows(PaymentBadRequestException.class, () -> {
            partCancelPaymentUseCase.partCancelPayment(partCancelPaymentCommand);
        });

        price = 6600;
        tax = 700L;
        PartCancelPaymentCommand cancelPaymentCommand = getPartCancelPaymentCommand(managementId, price, tax);

        assertThatThrownBy(() -> partCancelPaymentUseCase.partCancelPayment(cancelPaymentCommand)).isInstanceOf(PaymentBadRequestException.class);

        // given
        tax = 600L;
        command = getPartCancelPaymentCommand(managementId, price, tax);
        // when
        partCancelPayment = partCancelPaymentUseCase.partCancelPayment(command);
        // then
        assertThat(partCancelPayment.originalManagementId()).isEqualTo(managementId);
        assertThat(partCancelPayment.price()).isEqualTo(price);
    }

    @Test
    @DisplayName("테스트 케이스 2")
    void test_case_2() {
        // given
        Payment payment = saveDefaultPayment(20000d, 909L);
        String managementId = payment.paymentId().getId();
        double price = 10000;
        Long tax = 0L;
        // when
        PartCancelPaymentCommand command = getPartCancelPaymentCommand(managementId, price, tax);
        Payment partCancelPayment = partCancelPaymentUseCase.partCancelPayment(command);
        // then
        assertThat(partCancelPayment.originalManagementId()).isEqualTo(managementId);
        assertThat(partCancelPayment.price()).isEqualTo(price);
        assertThat(partCancelPayment.tax()).isEqualTo(tax);

        // when
        PartCancelPaymentCommand paymentCommand = getPartCancelPaymentCommand(managementId, price, tax);
        assertThrows(PaymentBadRequestException.class, () -> {
            partCancelPaymentUseCase.partCancelPayment(paymentCommand);
        });

        tax = 909L;
        command = getPartCancelPaymentCommand(managementId, price, tax);
        partCancelPayment = partCancelPaymentUseCase.partCancelPayment(command);
        assertThat(partCancelPayment.originalManagementId()).isEqualTo(managementId);
        assertThat(partCancelPayment.price()).isEqualTo(price);
        assertThat(partCancelPayment.tax()).isEqualTo(tax);

    }

    @Test
    @DisplayName("테스트 케이스 3")
    void test_case_3() {
        // given
        Payment payment = saveDefaultPayment(20000d, null);
        String managementId = payment.paymentId().getId();
        double price = 10000;
        Long tax = 1000L;
        // when
        PartCancelPaymentCommand command = getPartCancelPaymentCommand(managementId, price, tax);
        Payment partCancelPayment = partCancelPaymentUseCase.partCancelPayment(command);
        assertThat(partCancelPayment.originalManagementId()).isEqualTo(managementId);
        assertThat(partCancelPayment.price()).isEqualTo(price);
        assertThat(partCancelPayment.tax()).isEqualTo(tax);

        tax = 909L;
        PartCancelPaymentCommand paymentCommand = getPartCancelPaymentCommand(managementId, price, tax);
        assertThrows(PaymentBadRequestException.class, () -> {
            partCancelPaymentUseCase.partCancelPayment(paymentCommand);
        });

        tax = null;
        command = getPartCancelPaymentCommand(managementId, price, tax);
        partCancelPayment = partCancelPaymentUseCase.partCancelPayment(command);
        assertThat(partCancelPayment.originalManagementId()).isEqualTo(managementId);
        assertThat(partCancelPayment.price()).isEqualTo(price);

    }

    @NotNull
    private static PartCancelPaymentCommand getPartCancelPaymentCommand(String managementId, double price, Long tax) {
        PartCancelPaymentCommand command = new PartCancelPaymentCommand();
        command.setManagementId(managementId);
        command.setCancelPrice(price);
        command.setCancelTax(tax);
        return command;
    }

    private Payment saveDefaultPayment(double price, Long tax) {
        PaymentCommand command = new PaymentCommand();
        command.setPrice(price);
        command.setTax(tax);
        command.setCardNumber("12345678910");
        command.setCvc("777");
        command.setInstallmentMonth(InstallmentMonth.ONE);
        command.setValidYmd("1229");
        return paymentUseCase.payment(command);
    }

}