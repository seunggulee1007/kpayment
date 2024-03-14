package com.kakaoinsurance.payment.application.port.in.payment;

import com.kakaoinsurance.payment.common.SelfValidating;
import com.kakaoinsurance.payment.domain.payment.Payment;
import com.kakaoinsurance.payment.domain.payment.PaymentId;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PartCancelPaymentCommand extends SelfValidating<PartCancelPaymentCommand> {

    /**
     * 관리 번호
     */
    @NotNull(message = "관리번호는 필수 입니다.")
    private String managementId;

    /**
     * 취소 금액
     */
    @NotNull(message = "취소 금액은 필수 입니다.")
    private Double cancelPrice;

    /**
     * 취소 부가가치세
     */
    private Long cancelTax;

    public Payment mapToDomain() {
        return Payment.builder()
            .paymentId(PaymentId.of(managementId))
            .price(cancelPrice)
            .tax(cancelTax)
            .build();
    }

}
