package com.kakaoinsurance.payment.adapter.in.web.payment;

import com.kakaoinsurance.payment.application.port.in.payment.CancelPaymentCommand;
import lombok.Data;

import static org.springframework.beans.BeanUtils.copyProperties;

/**
 * 카드 전체 취소 요청 객체
 *
 * @author seunggu.lee
 */
@Data
public class CancelPaymentRequest {

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

    public CancelPaymentCommand mapToCommand() {
        CancelPaymentCommand command = new CancelPaymentCommand();
        copyProperties(this, command);
        command.validateSelf();
        return command;
    }

}
