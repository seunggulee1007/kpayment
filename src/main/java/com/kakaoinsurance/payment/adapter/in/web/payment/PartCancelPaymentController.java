package com.kakaoinsurance.payment.adapter.in.web.payment;

import com.kakaoinsurance.payment.application.port.in.carddata.CardDataCommand;
import com.kakaoinsurance.payment.application.port.in.carddata.CardDataUseCase;
import com.kakaoinsurance.payment.application.port.in.payment.PartCancelPaymentUseCase;
import com.kakaoinsurance.payment.common.utils.ApiUtil;
import com.kakaoinsurance.payment.domain.payment.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kakaoinsurance.payment.common.utils.ApiUtil.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PartCancelPaymentController {

    private final PartCancelPaymentUseCase partCancelPaymentUseCase;
    private final CardDataUseCase cardDataUseCase;

    @PutMapping
    public ApiUtil.ApiResult<PartCancelPaymentResponse> partCancelPayment(@RequestBody PartCancelPaymentRequest request) {
        Payment payment = partCancelPaymentUseCase.partCancelPayment(request.mapToCommand());
        String cardInfo = cardDataUseCase.getCardInfo(CardDataCommand.mapToCommand(payment));
        return success(PartCancelPaymentResponse.of(payment.paymentId().getId(), cardInfo));
    }

}
