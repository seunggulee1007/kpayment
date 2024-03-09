package com.kakaoinsurance.payment.adapter.out.persistence.card;

import com.kakaoinsurance.payment.application.port.out.CardDataOutPort;
import com.kakaoinsurance.payment.common.annotations.ExternalAdapter;
import lombok.RequiredArgsConstructor;

/**
 * 카드 데이터 전송 어뎁터
 * 실제로라면 외부와 통신을 할 에정이기 때문에 {@link ExternalAdapter} 를 사용함.
 */
@ExternalAdapter
@RequiredArgsConstructor
public class CardDataPortAdapter implements CardDataOutPort {

    private final CardDataRepository cardDataRepository;

    @Override
    public void executeCardRequest(String cardData) {
        CardDataEntity entity = CardDataEntity.of(cardData);
        cardDataRepository.save(entity);
    }

}
