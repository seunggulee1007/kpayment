package com.kakaoinsurance.payment.adapter.in.web.payment;

import com.kakaoinsurance.payment.application.port.in.payment.PartCancelPaymentCommand;
import lombok.Data;

import static org.springframework.beans.BeanUtils.copyProperties;

@Data
public class PartCancelPaymentRequest {

    /**
     * 관리 번호
     */
    private String managementId;

    /**
     * 취소 금액
     */
    private Double cancelPrice;

    /**
     * 취소 부가가치세
     */
    private Long cancelTax;

    public PartCancelPaymentCommand mapToCommand() {
        PartCancelPaymentCommand command = new PartCancelPaymentCommand();
        copyProperties(this, command);
        command.validateSelf();
        return command;
    }

}
