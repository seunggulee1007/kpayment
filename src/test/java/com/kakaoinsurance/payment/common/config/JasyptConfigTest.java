package com.kakaoinsurance.payment.common.config;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class JasyptConfigTest {

    @Autowired
    private StringEncryptor jasyptStringEncryptor;

    @Test
    @DisplayName("암복호화 테스트")
    void encryptor() {
        // given
        String name = "kakaoinsurance";
        String encrypt = jasyptStringEncryptor.encrypt(name);
        // when
        String decrypted = jasyptStringEncryptor.decrypt(encrypt);
        // then
        assertThat(decrypted).isEqualTo(name);

    }

}