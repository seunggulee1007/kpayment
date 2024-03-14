package com.kakaoinsurance.payment.application.port.in.payment;

import com.kakaoinsurance.payment.common.container.RedisTestContainer;
import com.kakaoinsurance.payment.domain.payment.InstallmentMonth;
import com.kakaoinsurance.payment.domain.payment.Payment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("결제 조회 UseCase 테스트")
class GetPaymentUseCaseTest extends RedisTestContainer {

    @Autowired
    private GetPaymentUseCase getPaymentUseCase;

    @Autowired
    private PaymentUseCase paymentUseCase;

    @Test
    @DisplayName("조회 테스트")
    void setGetPayment() {

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
        Payment payment = paymentUseCase.payment(command);
        Payment paymentByManagementId = getPaymentUseCase.getPaymentByManagementId(payment.paymentId().getId());

        // then
        assertThat(paymentByManagementId).isNotNull();
        assertThat(paymentByManagementId.cardNumber()).isEqualTo(cardNumber);
        assertThat(paymentByManagementId.cvc()).isEqualTo(cvc);
        assertThat(paymentByManagementId.validYmd()).isEqualTo(validYmd);
        assertThat(paymentByManagementId.price()).isEqualTo(price);
        assertThat(paymentByManagementId.tax()).isEqualTo(tax);
        assertThat(paymentByManagementId.installmentMonth()).isEqualTo(installmentMonth);
    }

}