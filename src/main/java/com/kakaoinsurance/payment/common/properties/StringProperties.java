package com.kakaoinsurance.payment.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("string")
public class StringProperties {

    private String cardEncryptSeparator;

    private String masking;

}
