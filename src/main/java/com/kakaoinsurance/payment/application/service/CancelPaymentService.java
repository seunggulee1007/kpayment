package com.kakaoinsurance.payment.application.service;

import com.kakaoinsurance.payment.application.port.in.CancelPaymentCommand;
import com.kakaoinsurance.payment.application.port.in.CancelPaymentUseCase;
import com.kakaoinsurance.payment.application.port.out.CancelPaymentOutPort;
import com.kakaoinsurance.payment.common.annotations.UseCase;
import com.kakaoinsurance.payment.domain.Payment;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class CancelPaymentService implements CancelPaymentUseCase {

    private final CancelPaymentOutPort cancelPaymentOutPort;

    @Override
    public Payment cancelPayment(CancelPaymentCommand command) {
        return cancelPaymentOutPort.cancelPayment(command.mapToDomain());
    }

}
