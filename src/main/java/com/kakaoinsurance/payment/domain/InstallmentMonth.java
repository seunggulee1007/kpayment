package com.kakaoinsurance.payment.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kakaoinsurance.payment.common.advice.exceptions.InvalidInstallmentMonthException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum InstallmentMonth {

    ZERO("00"),
    ONE("01"),
    TWO("02"),
    THREE("03"),
    FOUR("04"),
    FIVE("05"),
    SIX("06"),
    SEVEN("07"),
    EIGHT("08"),
    NINE("09"),
    TEN("10"),
    ELEVEN("11"),
    TWELVE("12"),
    ;
    private final String month;

    @JsonCreator
    public static InstallmentMonth creator(String month) {
        for (InstallmentMonth installmentPeriod : values()) {
            if (month.equals(installmentPeriod.month)) {
                return installmentPeriod;
            }
        }
        throw new InvalidInstallmentMonthException("존재하지 않는 할부 개월입니다.");
    }

    @JsonValue
    public String getStatus() {
        return this.month;
    }

}
