package com.kakaoinsurance.payment.application.port.in;

import com.kakaoinsurance.payment.common.SelfValidating;
import lombok.Getter;

@Getter
public class GetMemberQuery extends SelfValidating<GetMemberQuery> {

    private Long accountId;

    private String password;

    public static GetMemberQuery of(Long accountId, String password) {
        GetMemberQuery getMemberQuery = new GetMemberQuery();
        getMemberQuery.accountId = accountId;
        getMemberQuery.password = password;
        getMemberQuery.validateSelf();
        return getMemberQuery;
    }

}
