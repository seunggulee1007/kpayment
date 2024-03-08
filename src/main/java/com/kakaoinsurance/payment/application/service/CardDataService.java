package com.kakaoinsurance.payment.application.service;

import com.kakaoinsurance.payment.application.port.in.CardDataUseCase;
import com.kakaoinsurance.payment.application.port.in.CardInfoCommand;
import com.kakaoinsurance.payment.common.advice.exceptions.NotValidCardDataException;
import com.kakaoinsurance.payment.common.annotations.UseCase;
import com.kakaoinsurance.payment.common.properties.StringProperties;
import com.kakaoinsurance.payment.domain.CardData;
import com.kakaoinsurance.payment.domain.CommonHeader;
import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;

import java.util.StringTokenizer;

@UseCase
@RequiredArgsConstructor
public class CardDataService implements CardDataUseCase {

    private final StringEncryptor jasyptStringEncryptor;
    private final StringProperties stringProperties;

    public String getCardInfo(CardInfoCommand command) {

        StringTokenizer decryptedCardDatum = getDecryptedCardDatum(command.getCardData());
        CommonHeader header = CommonHeader.builder()
            .managementId(command.getManagementId())
            .dataKind(command.getPaymentKind())
            .build().calculateTotalLength();
        CardData cardData = CardData.builder()
            .cardNumber(decryptedCardDatum.nextToken())
            .installmentMonth(command.getInstallmentMonth())
            .validYmd(decryptedCardDatum.nextToken())
            .cvc(decryptedCardDatum.nextToken())
            .price(command.getPrice())
            .tax(command.getTax())
            .originalManagementId(command.getOriginalManagementId())
            .encryptedCardInfo(command.getCardData())
            .build();
        return header.getCardInfo(cardData);
    }

    private StringTokenizer getDecryptedCardDatum(String cardData) {
        String decrypted = jasyptStringEncryptor.decrypt(cardData);
        String cardEncryptSeparator = stringProperties.getCardEncryptSeparator();
        StringTokenizer stringTokenizer = new StringTokenizer(decrypted, cardEncryptSeparator);
        if (stringTokenizer.countTokens() != 3)
            throw new NotValidCardDataException();
        return stringTokenizer;
    }

}
