package com.kakaoinsurance.payment.adapter.in.web;

import com.kakaoinsurance.payment.application.port.in.PaymentCommand;
import com.kakaoinsurance.payment.domain.InstallmentMonth;
import lombok.Data;

import static org.springframework.beans.BeanUtils.copyProperties;

/**
 * 클라리언트에서 받을 요청부
 *
 * @author seunggu.lee
 */
@Data
public class PaymentRequest {

    /**
     * 카드 번호 ( 10 ~ 16 자리 숫자 )
     */
    private String cardNumber;

    /**
     * 유효기간 ( 4자리 숫자 , mmyy )
     */
    private String validYmd;

    /**
     * cvc (3자리 숫자 )
     */
    private String cvc;

    /**
     * 할부개월수: 0(일시불), 1 ~ 12
     */
    private InstallmentMonth installmentMonth;

    /**
     * 결제금액(100원 이상, 10억원 이하, 숫자)
     */
    private Double price;

    /**
     * 부가 가치세 ( optional )
     */
    private Long tax;

    public PaymentCommand mapToCommand() {
        PaymentCommand command = new PaymentCommand();
        copyProperties(this, command);
        return command;
    }

}
