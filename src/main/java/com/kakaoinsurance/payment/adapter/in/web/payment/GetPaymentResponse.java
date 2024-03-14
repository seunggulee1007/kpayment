package com.kakaoinsurance.payment.adapter.in.web.payment;

import com.kakaoinsurance.payment.domain.payment.Payment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetPaymentResponse {

    private String managementId;

    private String cardNumber;

    private String validYmd;

    private String cvc;

    private String paymentKind;

    private double price;

    private Long tax;

    private String paidBy;

    private LocalDateTime paidAt;

    public static GetPaymentResponse from(Payment payment) {
        GetPaymentResponse response = new GetPaymentResponse();
        response.managementId = payment.paymentId().getId();
        response.cardNumber = payment.cardNumber();
        response.validYmd = payment.validYmd();
        response.cvc = payment.cvc();
        response.paymentKind = payment.paymentKind().name();
        response.price = payment.price();
        response.tax = payment.tax();
        response.paidBy = payment.paidBy();
        response.paidAt = payment.paidAt();
        return response;
    }

}
