package com.kakaoinsurance.payment.application.port.in.payment;

import com.kakaoinsurance.payment.common.container.RedisTestContainer;
import com.kakaoinsurance.payment.common.properties.StringProperties;
import com.kakaoinsurance.payment.domain.payment.InstallmentMonth;
import com.kakaoinsurance.payment.domain.payment.Payment;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.StringTokenizer;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("결제 useCase 테스트")
class PaymentUseCaseTest extends RedisTestContainer {

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

}