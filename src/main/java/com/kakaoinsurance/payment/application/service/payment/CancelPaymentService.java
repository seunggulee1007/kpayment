package com.kakaoinsurance.payment.application.service.payment;

import com.kakaoinsurance.payment.application.port.in.payment.CancelPaymentCommand;
import com.kakaoinsurance.payment.application.port.in.payment.CancelPaymentUseCase;
import com.kakaoinsurance.payment.application.port.out.payment.CancelPaymentOutPort;
import com.kakaoinsurance.payment.common.advice.exceptions.AlreadyPaymentException;
import com.kakaoinsurance.payment.common.annotations.UseCase;
import com.kakaoinsurance.payment.common.config.RedisRepository;
import com.kakaoinsurance.payment.domain.payment.Payment;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class CancelPaymentService implements CancelPaymentUseCase {

    private final CancelPaymentOutPort cancelPaymentOutPort;
    private final RedisRepository redisRepository;

    @Override
    public Payment cancelPayment(CancelPaymentCommand command) {
        if (Boolean.FALSE.equals(redisRepository.lock(command.getManagementId()))) {
            throw new AlreadyPaymentException();
        }
        try {
            return cancelPaymentOutPort.cancelPayment(command.mapToDomain());
        } finally {
            redisRepository.unlock(command.getManagementId());
        }
    }

}
