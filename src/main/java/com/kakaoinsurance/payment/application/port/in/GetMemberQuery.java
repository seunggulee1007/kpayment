package com.kakaoinsurance.payment.application.port.in;

import com.kakaoinsurance.payment.common.SelfValidating;
import lombok.Getter;

@Getter
public class GetMemberQuery extends SelfValidating<GetMemberQuery> {

    private Long memberId;

    private String password;

    public static GetMemberQuery of(Long memberId, String password) {
        GetMemberQuery getMemberQuery = new GetMemberQuery();
        getMemberQuery.memberId = memberId;
        getMemberQuery.password = password;
        getMemberQuery.validateSelf();
        return getMemberQuery;
    }

}
