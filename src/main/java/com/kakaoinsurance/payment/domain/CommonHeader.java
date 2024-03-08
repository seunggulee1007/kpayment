package com.kakaoinsurance.payment.domain;

import com.kakaoinsurance.payment.common.annotations.DataType;
import com.kakaoinsurance.payment.common.annotations.PayType;
import lombok.Builder;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 공통 헤더
 */
@Builder
public record CommonHeader(

    /* 데이터 길이 */
    @PayType(dataType = DataType.N, length = 4, exclude = true, order = 1)
    String dataLength,

    /* 데이터 구분 */
    @PayType(length = 10, order = 2)
    String dataKind,

    /* 관리번호 */
    @PayType(length = 20, order = 3)
    String managementId
) {

    public CommonHeader calculateTotalLength() {
        int totalLength = 0;
        totalLength += sumLength(this.getClass().getDeclaredFields());
        totalLength += sumLength(CardData.class.getDeclaredFields());
        return CommonHeader.builder()
            .dataLength(Integer.toString(totalLength))
            .dataKind(this.dataKind)
            .managementId(this.managementId)
            .build();
    }

    public int sumLength(Field[] fields) {
        int sum = 0;
        for (Field field : fields) {
            PayType payType = field.getAnnotation(PayType.class);
            if (!payType.exclude()) {
                sum += payType.length();
            }
        }
        return sum;
    }

    public String getCardInfo(CardData cardData) {
        StringBuilder stringBuilder = new StringBuilder();
        Field[] declaredFields = this.getClass().getDeclaredFields();
        appendDataToStringBuilder(declaredFields, stringBuilder, this);
        declaredFields = CardData.class.getDeclaredFields();
        appendDataToStringBuilder(declaredFields, stringBuilder, cardData);
        return stringBuilder.toString();
    }

    private void appendDataToStringBuilder(Field[] declaredFields, StringBuilder stringBuilder, Object object
    ) {
        Arrays.sort(declaredFields, Comparator.comparingInt(f -> f.getAnnotation(PayType.class).order()));
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            PayType payType = declaredField.getAnnotation(PayType.class);
            String length = Integer.toString(payType.length());
            DataType dataType = payType.dataType();
            try {
                appendData(stringBuilder, object, declaredField, dataType, length);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void appendData(StringBuilder stringBuilder, Object object, Field declaredField, DataType dataType, String length) throws
        IllegalAccessException {
        String field = (String)declaredField.get(object);
        if (field == null)
            field = "";
        stringBuilder.append(switch (dataType) {
            case O -> String.format("%0".concat(length)
                                        .concat("d"), Integer.parseInt(field));
            case N -> String.format("%".concat(length)
                                        .concat("s"), field);
            case L, S -> String.format("%-".concat(length)
                                           .concat("s"), field);
        });
    }

}
