package com.kakaoinsurance.payment.application.service;

import com.kakaoinsurance.payment.application.port.in.PaymentCommand;
import com.kakaoinsurance.payment.application.port.in.PaymentUseCase;
import com.kakaoinsurance.payment.application.port.out.PaymentOutPort;
import com.kakaoinsurance.payment.common.annotations.UseCase;
import com.kakaoinsurance.payment.common.properties.StringProperties;
import com.kakaoinsurance.payment.domain.Payment;
import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;

@UseCase
@RequiredArgsConstructor
public class PaymentService implements PaymentUseCase {

    private final StringProperties stringProperties;
    private final StringEncryptor jasyptStringEncryptor;
    private final PaymentOutPort paymentOutPort;

    @Override
    public Payment payment(PaymentCommand command) {
        String cardData = command.getCardData(stringProperties.getCardEncryptSeparator());
        cardData = jasyptStringEncryptor.encrypt(cardData);
        return paymentOutPort.savePayment(command.mapToDomain(cardData));
    }

}
