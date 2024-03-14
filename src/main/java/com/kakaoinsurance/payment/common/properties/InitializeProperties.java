package com.kakaoinsurance.payment.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("initialize")
public class InitializeProperties {

    private String nickname;
    
    private String email;

    private String password;

}
