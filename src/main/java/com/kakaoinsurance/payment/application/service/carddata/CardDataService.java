package com.kakaoinsurance.payment.application.service.carddata;

import com.kakaoinsurance.payment.application.port.in.carddata.CardDataCommand;
import com.kakaoinsurance.payment.application.port.in.carddata.CardDataUseCase;
import com.kakaoinsurance.payment.application.port.out.carddata.CardDataOutPort;
import com.kakaoinsurance.payment.common.advice.exceptions.NotValidCardDataException;
import com.kakaoinsurance.payment.common.annotations.UseCase;
import com.kakaoinsurance.payment.common.properties.StringProperties;
import com.kakaoinsurance.payment.domain.carddata.CardData;
import com.kakaoinsurance.payment.domain.carddata.CommonHeader;
import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.transaction.annotation.Transactional;

import java.util.StringTokenizer;

@UseCase
@RequiredArgsConstructor
public class CardDataService implements CardDataUseCase {

    private final StringEncryptor jasyptStringEncryptor;
    private final StringProperties stringProperties;
    private final CardDataOutPort cardDataOutPort;

    @Transactional
    public String getCardInfo(CardDataCommand command) {

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
        String cardInfo = header.getCardInfo(cardData);
        cardDataOutPort.executeCardRequest(cardInfo, command.getManagementId());
        return cardInfo;
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
