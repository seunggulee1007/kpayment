package com.kakaoinsurance.payment.application.port.out.carddata;

public interface CardDataOutPort {

    void executeCardRequest(String cardData, String managementId);

}
