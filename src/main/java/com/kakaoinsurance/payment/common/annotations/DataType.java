package com.kakaoinsurance.payment.common.annotations;

/**
 * 카드사에 전달할 필드들의 데이터 타입
 */
public enum DataType {

    /**
     * 우측으로 정렬, 빈 자리 공백, ex) 4자리 숫자 : 3 -> "___3"
     */
    L,
    /**
     * 우측으로 정렬, 빈 자리 0, ex) 4자리 숫자(0) : 3 -> "0003"
     */
    O,
    /**
     * 좌측으로 정렬, 빈 자리 공백, ex) 4자리 숫자(L) : 3 -> "3___"
     */
    N,
    /**
     * 좌측으로 정렬, 빈 자리 공백, ex) 10자리 문자 : HOMEWORK -> "HOMEWORK__"
     */
    S
}
