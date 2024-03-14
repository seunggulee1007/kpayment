package com.kakaoinsurance.payment.adapter.in.web.payment;

import lombok.Data;

@Data
public class PartCancelPaymentResponse {

    /**
     * 관리번호 (unique id, 20자리 )
     */
    private String managementNumber;

    /**
     * 카드사에 전달한 string 데이터 : "공통 헤더부문" + "데이터 부문"
     */
    private String deliveredCardData;

    public static PartCancelPaymentResponse of(String managementNumber, String deliveredCardData) {
        PartCancelPaymentResponse response = new PartCancelPaymentResponse();
        response.managementNumber = managementNumber;
        response.deliveredCardData = deliveredCardData;
        return response;
    }

}
