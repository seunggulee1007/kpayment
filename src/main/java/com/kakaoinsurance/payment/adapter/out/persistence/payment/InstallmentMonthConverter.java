package com.kakaoinsurance.payment.adapter.out.persistence.payment;

import com.kakaoinsurance.payment.domain.InstallmentMonth;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class InstallmentMonthConverter implements AttributeConverter<InstallmentMonth, String> {

    @Override
    public String convertToDatabaseColumn(InstallmentMonth installmentMonth) {
        return installmentMonth.getStatus();
    }

    @Override
    public InstallmentMonth convertToEntityAttribute(String month) {
        return InstallmentMonth.creator(month);
    }

}
