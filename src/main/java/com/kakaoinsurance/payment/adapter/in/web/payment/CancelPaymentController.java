package com.kakaoinsurance.payment.adapter.in.web.payment;

import com.kakaoinsurance.payment.application.port.in.carddata.CardDataCommand;
import com.kakaoinsurance.payment.application.port.in.carddata.CardDataUseCase;
import com.kakaoinsurance.payment.application.port.in.payment.CancelPaymentUseCase;
import com.kakaoinsurance.payment.common.utils.ApiUtil;
import com.kakaoinsurance.payment.domain.payment.Payment;
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
        String cardInfo = cardDataUseCase.getCardInfo(CardDataCommand.mapToCommand(payment));
        return success(CancelPaymentResponse.of(payment.paymentId().getId(), cardInfo));
    }

}
