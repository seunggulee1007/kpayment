package com.kakaoinsurance.payment.adapter.out.persistence.payment;

import com.kakaoinsurance.payment.application.port.out.member.GetPaymentOutPort;
import com.kakaoinsurance.payment.application.port.out.payment.CancelPaymentOutPort;
import com.kakaoinsurance.payment.application.port.out.payment.PartCancelPaymentOutPort;
import com.kakaoinsurance.payment.application.port.out.payment.PaymentOutPort;
import com.kakaoinsurance.payment.common.advice.exceptions.NotPresentPaymentException;
import com.kakaoinsurance.payment.common.annotations.PersistenceAdapter;
import com.kakaoinsurance.payment.domain.payment.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@PersistenceAdapter
@RequiredArgsConstructor
public class PaymentOutPortAdapter implements PaymentOutPort, CancelPaymentOutPort, GetPaymentOutPort, PartCancelPaymentOutPort {

    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
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

    @Override
    @Transactional
    public Payment partCancelPayment(Payment payment) {
        String paymentId = payment.paymentId().getId();
        PaymentEntity entity = paymentRepository.findByPaymentId(paymentId).orElseThrow(NotPresentPaymentException::new);
        PaymentEntity partCancelEntity = entity.partCancel(payment);
        paymentRepository.save(partCancelEntity);
        return partCancelEntity.mapToDomain();
    }

}
