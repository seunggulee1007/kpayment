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

    @Column(length = 20)
    private String managementId;

    private String cardData;

    public static CardDataEntity of(String cardData, String managementId) {
        CardDataEntity entity = new CardDataEntity();
        entity.managementId = managementId;
        entity.cardData = cardData;
        return entity;
    }

}
