package com.kakaoinsurance.payment.adapter.in.web.payment;

import lombok.Data;

/**
 * <p>카드 결제가 성공할 경우 클라이언트에게 보내질 정보</p>
 * json 으로 내릴 경우 getter 가 필요하지 않으며, 만들어진 이후에는 변경이 필요 없으므로 setter 역시 필요 없다.
 *
 * @author seunggu .lee
 */
@Data
public class PaymentResponse {

    /**
     * 관리번호 (unique id, 20자리 )
     */
    private String managementNumber;

    /**
     * 카드사에 전달한 string 데이터 : "공통 헤더부문" + "데이터 부문"
     */
    private String deliveredCardData;

    public static PaymentResponse of(String managementNumber, String deliveredCardData) {
        PaymentResponse response = new PaymentResponse();
        response.managementNumber = managementNumber;
        response.deliveredCardData = deliveredCardData;
        return response;
    }

}
