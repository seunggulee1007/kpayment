package com.kakaoinsurance.payment.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberId {

    private final String id;

    public static MemberId of(String id) {
        return new MemberId(id);
    }

    public static MemberId of(Long id) {
        return new MemberId(Long.toString(id));
    }

}
