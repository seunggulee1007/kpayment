package com.kakaoinsurance.payment.application.service.payment;

import com.kakaoinsurance.payment.application.port.in.payment.GetPaymentUseCase;
import com.kakaoinsurance.payment.application.port.in.payment.PartCancelPaymentCommand;
import com.kakaoinsurance.payment.application.port.in.payment.PaymentCommand;
import com.kakaoinsurance.payment.common.advice.exceptions.PaymentBadRequestException;
import com.kakaoinsurance.payment.common.container.RedisTestContainer;
import com.kakaoinsurance.payment.domain.payment.InstallmentMonth;
import com.kakaoinsurance.payment.domain.payment.Payment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.kakaoinsurance.payment.common.DefaultMessage.CANCEL_PRICE_LATHER_THEN_PRICE;
import static com.kakaoinsurance.payment.common.DefaultMessage.NOT_VALID_TAX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@DisplayName("부분 취소 서비스 테스트")
@SpringBootTest
class PartCancelPaymentServiceTest extends RedisTestContainer {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private GetPaymentUseCase getPaymentUseCase;
    @Autowired
    private PartCancelPaymentService partCancelPaymentService;

    @Test
    @DisplayName("취소 동시성 테스트")
    void cancelTest() throws InterruptedException {
        // given
        double price = 10000d;
        String managementId = saveDefaultPayment(price, null);
        int threadCount = 10;
        // when
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        PartCancelPaymentCommand command = new PartCancelPaymentCommand();
        command.setManagementId(managementId);
        command.setCancelPrice(price / threadCount);
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    partCancelPaymentService.partCancelPayment(command);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then

        Payment remainPayment = getPaymentUseCase.getPaymentByManagementId(managementId);
        assertEquals(0, remainPayment.remainPrice());
        assertEquals(0, remainPayment.remainTax());

    }

    @Test
    @DisplayName("부분 결제 취소 실패 - 금액")
    void part_cancel_fail_price() {
        // given
        String managementId = saveDefaultPayment(10000d, 500L);
        double cancelPrice = 11000d;
        PartCancelPaymentCommand command = new PartCancelPaymentCommand();
        command.setManagementId(managementId);
        command.setCancelPrice(cancelPrice);
        // when
        PaymentBadRequestException exception = assertThrows(PaymentBadRequestException.class, () -> {
            partCancelPaymentService.partCancelPayment(command);
        });
        // then
        assertThat(exception.getMessage()).isEqualTo(CANCEL_PRICE_LATHER_THEN_PRICE.getMessage());

        // given
        cancelPrice = 10000d;
        Long cancelTax = 550L;
        command.setCancelPrice(cancelPrice);
        command.setCancelTax(cancelTax);

        // when
        exception = assertThrows(PaymentBadRequestException.class, () -> {
            partCancelPaymentService.partCancelPayment(command);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo(NOT_VALID_TAX.getMessage());

        // given
        cancelTax = 450L;
        command.setCancelPrice(cancelPrice);
        command.setCancelTax(cancelTax);

        // when
        exception = assertThrows(PaymentBadRequestException.class, () -> {
            partCancelPaymentService.partCancelPayment(command);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo(NOT_VALID_TAX.getMessage());

    }

    private String saveDefaultPayment(Double price, Long tax) {
        PaymentCommand command = new PaymentCommand();
        command.setCardNumber("30021403202592");
        command.setValidYmd("1226");
        command.setCvc("777");
        command.setInstallmentMonth(InstallmentMonth.ZERO);
        command.setPrice(price);
        command.setTax(tax);
        Payment payment = paymentService.payment(command);
        return payment.paymentId().getId();
    }

}