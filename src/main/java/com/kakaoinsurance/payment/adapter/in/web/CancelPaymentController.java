package com.kakaoinsurance.payment.adapter.in.web;

import com.kakaoinsurance.payment.application.port.in.CancelPaymentUseCase;
import com.kakaoinsurance.payment.application.port.in.CardDataUseCase;
import com.kakaoinsurance.payment.application.port.in.CardInfoCommand;
import com.kakaoinsurance.payment.common.utils.ApiUtil;
import com.kakaoinsurance.payment.domain.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kakaoinsurance.payment.common.utils.ApiUtil.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class CancelPaymentController {

    private final CancelPaymentUseCase cancelPaymentUseCase;
    private final CardDataUseCase cardDataUseCase;

    @DeleteMapping
    public ApiUtil.ApiResult<CancelPaymentResponse> cancelPayment(@RequestBody CancelPaymentRequest request) {
        Payment payment = cancelPaymentUseCase.cancelPayment(request.mapToCommand());
        String cardInfo = cardDataUseCase.getCardInfo(CardInfoCommand.mapToCommand(payment));
        return success(CancelPaymentResponse.of(payment.paymentId().getId(), cardInfo));
    }

}
