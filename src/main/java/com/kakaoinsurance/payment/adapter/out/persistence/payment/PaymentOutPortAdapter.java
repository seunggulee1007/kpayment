package com.kakaoinsurance.payment.adapter.out.persistence.payment;

import com.kakaoinsurance.payment.application.port.out.CancelPaymentOutPort;
import com.kakaoinsurance.payment.application.port.out.GetPaymentOutPort;
import com.kakaoinsurance.payment.application.port.out.PaymentOutPort;
import com.kakaoinsurance.payment.common.advice.exceptions.NotPresentPaymentException;
import com.kakaoinsurance.payment.common.annotations.PersistenceAdapter;
import com.kakaoinsurance.payment.domain.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@PersistenceAdapter
@RequiredArgsConstructor
public class PaymentOutPortAdapter implements PaymentOutPort, CancelPaymentOutPort, GetPaymentOutPort {

    private final PaymentRepository paymentRepository;

    @Override
    public Payment savePayment(Payment payment) {
        PaymentEntity entity = PaymentEntity.mapToEntity(payment);
        paymentRepository.save(entity);
        return entity.mapToDomain();
    }

    @Override
    @Transactional
    public Payment cancelPayment(Payment payment) {
        String paymentId = payment.paymentId().getId();
        PaymentEntity entity = paymentRepository.findByPaymentId(paymentId).orElseThrow(NotPresentPaymentException::new);
        PaymentEntity cancelEntity = entity.cancel(payment);
        paymentRepository.save(cancelEntity);
        return cancelEntity.mapToDomain();
    }

    @Override
    public Payment findById(String paymentId) {
        PaymentEntity entity = paymentRepository.findByPaymentId(paymentId).orElseThrow(NotPresentPaymentException::new);
        return entity.mapToDomain();
    }

}
