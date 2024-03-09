package com.kakaoinsurance.payment.adapter.out.persistence.card;

import com.kakaoinsurance.payment.adapter.out.persistence.CreatedEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "card_data")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardDataEntity extends CreatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardData;

    public static CardDataEntity of(String cardData) {
        CardDataEntity entity = new CardDataEntity();
        entity.cardData = cardData;
        return entity;
    }

}
