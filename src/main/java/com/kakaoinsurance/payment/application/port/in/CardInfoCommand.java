package com.kakaoinsurance.payment.application.port.in;

import com.kakaoinsurance.payment.domain.Payment;
import lombok.Getter;

import java.text.DecimalFormat;

@Getter
public class CardInfoCommand {

    private String managementId;

    private String paymentKind;

    private String cardData;

    private String installmentMonth;

    private String price;

    private String tax;

    private String originalManagementId;

    public static CardInfoCommand mapToCommand(Payment payment) {
        CardInfoCommand command = new CardInfoCommand();
        DecimalFormat decimalFormat = new DecimalFormat("#");
        command.managementId = payment.paymentId().getId();
        command.paymentKind = payment.paymentKind().name();
        command.cardData = payment.cardData();
        command.installmentMonth = payment.installmentMonth().getStatus();
        command.price = decimalFormat.format(payment.price());
        command.tax = decimalFormat.format(payment.tax());
        command.originalManagementId = payment.originalManagementId();
        return command;
    }

}
