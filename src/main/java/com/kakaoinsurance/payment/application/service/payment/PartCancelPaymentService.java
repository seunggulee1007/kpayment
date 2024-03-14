package com.kakaoinsurance.payment.application.service.payment;

import com.kakaoinsurance.payment.application.port.in.payment.PartCancelPaymentCommand;
import com.kakaoinsurance.payment.application.port.in.payment.PartCancelPaymentUseCase;
import com.kakaoinsurance.payment.application.port.out.payment.PartCancelPaymentOutPort;
import com.kakaoinsurance.payment.common.annotations.RedissonLock;
import com.kakaoinsurance.payment.common.annotations.UseCase;
import com.kakaoinsurance.payment.domain.payment.Payment;
import lombok.RequiredArgsConstructor;

/**
 * 결제 부분취소 서비스
 * 동시 부분취소가 될 수 없도록 redisson 을 이용해서 Lock 처리를 한다.
 * aop 를 활용한 {@link com.kakaoinsurance.payment.common.aspect.RedissonLockAspect} 로 Lock 처리를 한다.
 *
 * @author seunggu.lee
 */
@UseCase
@RequiredArgsConstructor
public class PartCancelPaymentService implements PartCancelPaymentUseCase {

    private final PartCancelPaymentOutPort partCancelPaymentOutPort;

    @Override
    @RedissonLock(value = "#command.getManagementId()")
    public Payment partCancelPayment(PartCancelPaymentCommand command) {
        return partCancelPaymentOutPort.partCancelPayment(command.mapToDomain());
    }

}
