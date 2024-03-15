package com.kakaoinsurance.payment.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtil {

    /**
     * 부가 가치세를 계산한다.
     * <p>자동계산 수식 : 결제금액 / 11, 소수점이하 반올림</p>
     * <p>결제금액이 1,000원일 경우, 자동계산된 부가가치세는 91원입니다.</p>
     */
    public static Long calculateTax(Long tax, Double price) {
        if (tax == null) {
            return Math.round(price / 11);
        }
        return tax;
    }

    /**
     * 앞 6자리와 뒤 3자리를 제외한 나머지를 마스킹처리
     * <p>(?<=.{6}): 문자열의 시작부터 6개 문자 이후의 위치를 의미</p>
     * <p>.: 임의의 한 문자를 의미</p>
     * <p>(?=.{3}): 해당 위치에서 끝까지 3개 문자를 남겨두는 위치를 의미</p>
     *
     * @param number 카드 번호
     * @return 마스킹 처리된 카드 번호
     */
    public static String maskString(String number, String masking) {
        if (!StringUtils.hasLength(number) || number.length() <= 9) {
            return number;
        }
        return number.replaceAll("(?<=.{6}).(?=.{3})", masking);
    }

}
