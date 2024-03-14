package com.kakaoinsurance.payment.application.service.payment;

import com.kakaoinsurance.payment.application.port.in.payment.PaymentCommand;
import com.kakaoinsurance.payment.application.port.in.payment.PaymentUseCase;
import com.kakaoinsurance.payment.application.port.out.payment.PaymentOutPort;
import com.kakaoinsurance.payment.common.advice.exceptions.AlreadyPaymentException;
import com.kakaoinsurance.payment.common.annotations.UseCase;
import com.kakaoinsurance.payment.common.config.RedisRepository;
import com.kakaoinsurance.payment.common.properties.StringProperties;
import com.kakaoinsurance.payment.domain.payment.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class PaymentService implements PaymentUseCase {

    private final StringProperties stringProperties;
    private final StringEncryptor jasyptStringEncryptor;
    private final PaymentOutPort paymentOutPort;
    private final RedisRepository redisRepository;

    @Override
    public Payment payment(PaymentCommand command) {

        if (Boolean.FALSE.equals(redisRepository.lock(command.getCardNumber()))) {
            throw new AlreadyPaymentException();
        }

        try {
            String cardData = command.getCardData(stringProperties.getCardEncryptSeparator());
            cardData = jasyptStringEncryptor.encrypt(cardData);
            return paymentOutPort.savePayment(command.mapToDomain(cardData));
        } finally {
            redisRepository.unlock(command.getCardNumber());
        }

    }

}
