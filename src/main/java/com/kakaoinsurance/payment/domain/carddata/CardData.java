package com.kakaoinsurance.payment.domain.carddata;

import com.kakaoinsurance.payment.common.annotations.DataType;
import com.kakaoinsurance.payment.common.annotations.PayType;
import lombok.Builder;

@Builder
public record CardData(
    /* 카드 번호 */
    @PayType(dataType = DataType.L, length = 20, order = 1)
    String cardNumber,
    /* 할부개월수 */
    @PayType(dataType = DataType.O, length = 2, order = 2)
    String installmentMonth,
    /* 카드 유효기간 */
    @PayType(dataType = DataType.L, length = 4, order = 3)
    String validYmd,
    /* cvc */
    @PayType(dataType = DataType.L, length = 3, order = 4)
    String cvc,
    /* 거래금액 */
    @PayType(dataType = DataType.N, length = 10, order = 5)
    String price,
    /* 부가가치세 */
    @PayType(dataType = DataType.O, length = 10, order = 6)
    String tax,
    /* 원 거래 관리 번호 */
    @PayType(length = 20, order = 7)
    String originalManagementId,
    /* 암호화된 카드 정보 */
    @PayType(length = 300, order = 8)
    String encryptedCardInfo,
    /* 예비 필드 */
    @PayType(length = 47, order = 9)
    String extraField
) {

}
