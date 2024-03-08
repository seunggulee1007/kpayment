package com.kakaoinsurance.payment.application.service;

import com.kakaoinsurance.payment.application.port.in.PaymentCommand;
import com.kakaoinsurance.payment.application.port.in.PaymentUseCase;
import com.kakaoinsurance.payment.application.port.out.PaymentOutPort;
import com.kakaoinsurance.payment.common.annotations.UseCase;
import com.kakaoinsurance.payment.common.properties.StringProperties;
import com.kakaoinsurance.payment.domain.Payment;
import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.util.StringUtils;

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

    /**
     * 앞 6자리와 뒤 3자리를 제외한 나머지를 마스킹처리
     * <p>(?<=.{6}): 문자열의 시작부터 6개 문자 이후의 위치를 의미</p>
     * <p>.: 임의의 한 문자를 의미</p>
     * <p>(?=.{3}): 해당 위치에서 끝까지 3개 문자를 남겨두는 위치를 의미</p>
     *
     * @param number 카드 번호
     * @return 마스킹 처리된 카드 번호
     */
    private String maskString(String number) {
        if (!StringUtils.hasLength(number) || number.length() <= 9) {
            return number;
        }
        return number.replaceAll("(?<=.{6}).(?=.{3})", stringProperties.getMasking());
    }

}
