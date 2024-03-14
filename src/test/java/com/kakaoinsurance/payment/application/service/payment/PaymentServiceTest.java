package com.kakaoinsurance.payment.application.service.payment;

import com.kakaoinsurance.payment.application.port.in.payment.PaymentCommand;
import com.kakaoinsurance.payment.common.container.RedisTestContainer;
import com.kakaoinsurance.payment.domain.payment.InstallmentMonth;
import com.kakaoinsurance.payment.domain.payment.Payment;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("결제 서비스 테스트")
class PaymentServiceTest extends RedisTestContainer {

    @Autowired
    private PaymentService paymentService;

    @Test
    @DisplayName("동시성 테스트")
    void concurrency_payment() throws InterruptedException {
        // given
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        CountDownLatch latch = new CountDownLatch(2);
        // when
        List<AtomicReference<Payment>> paymentList = new ArrayList<>();
        String cardNumber = "30021403202592";
        for (int i = 0; i < 2; i++) {
            executorService.submit(() -> {
                try {
                    PaymentCommand command = getDefaultCommand(cardNumber);
                    AtomicReference<Payment> reference = new AtomicReference<>();
                    Payment saved = paymentService.payment(command);
                    reference.set(saved);
                    paymentList.add(reference);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        // then
        assertThat(paymentList).hasSize(1);
    }

    @NotNull
    private static PaymentCommand getDefaultCommand(String cardNumber) {
        PaymentCommand command = new PaymentCommand();
        command.setCardNumber(cardNumber);
        command.setValidYmd("1226");
        command.setCvc("777");
        command.setInstallmentMonth(InstallmentMonth.ZERO);
        command.setPrice(1000d);
        command.setTax(100L);
        return command;
    }

}