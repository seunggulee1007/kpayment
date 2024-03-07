package com.kakaoinsurance.payment.adapter.out.jwt;

import lombok.Data;

@Data
public class CredentialInfo {

    private String credential;

    public CredentialInfo(String password) {
        this.credential = password;
    }

}