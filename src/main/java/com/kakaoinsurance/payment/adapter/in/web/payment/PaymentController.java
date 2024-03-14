package com.kakaoinsurance.payment.adapter.in.web.payment;

import com.kakaoinsurance.payment.application.port.in.carddata.CardDataCommand;
import com.kakaoinsurance.payment.application.port.in.carddata.CardDataUseCase;
import com.kakaoinsurance.payment.application.port.in.payment.PaymentUseCase;
import com.kakaoinsurance.payment.common.utils.ApiUtil;
import com.kakaoinsurance.payment.domain.payment.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kakaoinsurance.payment.common.utils.ApiUtil.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentUseCase paymentUseCase;
    private final CardDataUseCase cardDataUseCase;

    @PostMapping
    public ApiUtil.ApiResult<PaymentResponse> payment(@RequestBody PaymentRequest request) {
        Payment payment = paymentUseCase.payment(request.mapToCommand());
        String cardInfo = cardDataUseCase.getCardInfo(CardDataCommand.mapToCommand(payment));
        return success(PaymentResponse.of(payment.paymentId().getId(), cardInfo));
    }

}
