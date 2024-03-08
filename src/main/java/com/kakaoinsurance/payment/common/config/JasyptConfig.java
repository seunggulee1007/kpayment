package com.kakaoinsurance.payment.common.config;

import com.ulisesbocchio.jasyptspringboot.properties.JasyptEncryptorConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JasyptConfig {

    private final JasyptEncryptorConfigurationProperties jasyptEncryptorConfigurationProperties;

    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        String password = jasyptEncryptorConfigurationProperties.getPassword();
        config.setPassword(password);
        config.setAlgorithm(jasyptEncryptorConfigurationProperties.getAlgorithm());
        config.setKeyObtentionIterations(jasyptEncryptorConfigurationProperties.getKeyObtentionIterations());
        config.setPoolSize(jasyptEncryptorConfigurationProperties.getPoolSize());
        config.setProvider(new BouncyCastleProvider());
        config.setSaltGeneratorClassName(jasyptEncryptorConfigurationProperties.getSaltGeneratorClassname());
        config.setStringOutputType(jasyptEncryptorConfigurationProperties.getStringOutputType());
        encryptor.setConfig(config);

        return encryptor;
    }

}