package com.kakaoinsurance.payment.application.service.payment;

import com.kakaoinsurance.payment.application.port.in.payment.GetPaymentUseCase;
import com.kakaoinsurance.payment.application.port.out.member.GetPaymentOutPort;
import com.kakaoinsurance.payment.common.advice.exceptions.PaymentBadRequestException;
import com.kakaoinsurance.payment.common.annotations.UseCase;
import com.kakaoinsurance.payment.common.properties.StringProperties;
import com.kakaoinsurance.payment.domain.payment.Payment;
import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;

import java.util.StringTokenizer;

@UseCase
@RequiredArgsConstructor
public class GetPaymentService implements GetPaymentUseCase {

    private final GetPaymentOutPort paymentOutPort;
    private final StringProperties stringProperties;
    private final StringEncryptor jasyptStringEncryptor;

    @Override
    public Payment getPaymentByManagementId(String managementId) {
        Payment payment = paymentOutPort.findById(managementId);
        String cardData = jasyptStringEncryptor.decrypt(payment.cardData());
        StringTokenizer tokenizer = new StringTokenizer(cardData, stringProperties.getCardEncryptSeparator());
        if (tokenizer.countTokens() != 3) {
            throw new PaymentBadRequestException("카드 암호화 데이터가 올바르지 않습니다.");
        }
        return Payment.builder()
            .paymentId(payment.paymentId())
            .cardNumber(tokenizer.nextToken())
            .validYmd(tokenizer.nextToken())
            .cvc(tokenizer.nextToken())
            .installmentMonth(payment.installmentMonth())
            .paymentKind(payment.paymentKind())
            .price(payment.price())
            .tax(payment.tax())
            .remainPrice(payment.remainPrice())
            .remainTax(payment.remainTax())
            .paidAt(payment.paidAt())
            .paidBy(payment.paidBy())
            .build();
    }

}
