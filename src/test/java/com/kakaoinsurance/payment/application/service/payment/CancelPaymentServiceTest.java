package com.kakaoinsurance.payment.application.service.payment;

import com.kakaoinsurance.payment.application.port.in.payment.CancelPaymentCommand;
import com.kakaoinsurance.payment.application.port.in.payment.PaymentCommand;
import com.kakaoinsurance.payment.common.container.RedisTestContainer;
import com.kakaoinsurance.payment.domain.payment.InstallmentMonth;
import com.kakaoinsurance.payment.domain.payment.Payment;
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
@DisplayName("전체 취소 service 테스트")
class CancelPaymentServiceTest extends RedisTestContainer {

    @Autowired
    private CancelPaymentService cancelPaymentService;
    @Autowired
    private PaymentService paymentService;

    @Test
    @DisplayName("전체 취소 동시성 테스트")
    void concurrency_cancel_test() throws InterruptedException {
        // given
        double price = 1000d;
        String managementId = saveDefaultPayment(price);
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        CountDownLatch latch = new CountDownLatch(2);
        // when
        List<AtomicReference<Payment>> paymentList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            executorService.submit(() -> {
                try {
                    CancelPaymentCommand command = new CancelPaymentCommand();
                    command.setManagementId(managementId);
                    command.setCancelPrice(price);
                    AtomicReference<Payment> reference = new AtomicReference<>();
                    reference.set(cancelPaymentService.cancelPayment(command));
                    paymentList.add(reference);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        // then
        assertThat(paymentList).hasSize(1);

    }

    private String saveDefaultPayment(double price) {
        PaymentCommand command = new PaymentCommand();
        command.setCardNumber("30021403202592");
        command.setValidYmd("1226");
        command.setCvc("777");
        command.setInstallmentMonth(InstallmentMonth.ZERO);
        command.setPrice(price);
        command.setTax(100L);
        Payment payment = paymentService.payment(command);
        return payment.paymentId().getId();
    }

}