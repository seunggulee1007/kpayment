package com.kakaoinsurance.payment.adapter.out.persistence.payment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DisplayName("결제 관리번호 생성 테스트")
class PaymentIdGeneratorTest {

    @Test
    @DisplayName("관리번호 20자리 테스트")
    void id_generation_test() {
        // given & when
        String generate = PaymentIdGenerator.generate();
        // then
        assertThat(generate).hasSize(20);

    }

    @Test
    @DisplayName("중복 테스트")
    void id_duplication_test() {
        // given
        // int repeatCount = 100; // 100 만번 테스트 해도 문제 없으나, 전체 테스트 속도에 영향을 미쳐서 주석처리
        int repeatCount = 10;
        int capacity = 100000;
        // when
        for (int i = 0; i < repeatCount; i++) {
            Set<String> ids = new HashSet<>(capacity);
            for (int j = 0; j < capacity; j++) {
                ids.add(PaymentIdGenerator.generate());
            }
            // then
            assertThat(ids).hasSize(capacity);
        }

    }

}