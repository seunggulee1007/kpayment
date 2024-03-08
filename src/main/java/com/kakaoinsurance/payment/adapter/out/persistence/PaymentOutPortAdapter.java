package com.kakaoinsurance.payment.adapter.out.persistence;

import com.kakaoinsurance.payment.application.port.out.PaymentOutPort;
import com.kakaoinsurance.payment.common.annotations.PersistenceAdapter;
import com.kakaoinsurance.payment.domain.Payment;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class PaymentOutPortAdapter implements PaymentOutPort {

    private final PaymentRepository paymentRepository;

    @Override
    public Payment savePayment(Payment payment) {
        PaymentEntity entity = PaymentEntity.mapToEntity(payment);
        paymentRepository.save(entity);
        return entity.mapToDomain();
    }

}
