package com.kakaoinsurance.payment.common.advice;

import com.kakaoinsurance.payment.common.advice.exceptions.*;
import com.kakaoinsurance.payment.common.utils.ApiUtil;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.kakaoinsurance.payment.common.utils.ApiUtil.fail;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ApiUtil.ApiResult<Void> defaultException(Exception e) {
        log.error("e :: {}, message :: {}", e.getClass().getName(), e.getMessage());
        return fail(e, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
        AlreadyPresentAccountException.class,
        NotMatchedPasswordException.class,
        NotValidMemberException.class,
        InvalidInstallmentMonthException.class,
        ConstraintViolationException.class,
        PaymentBadRequestException.class,
        AlreadyPaymentException.class
    })
    @ResponseStatus(BAD_REQUEST)
    public ApiUtil.ApiResult<Void> badRequest(Exception e) {
        log.error("e :: {}, message :: {}", e.getClass().getName(), e.getMessage());
        return fail(e, BAD_REQUEST);
    }

    /**
     * 리소스를 찾을수 없는 경우 내리는 응답.
     * 인증받지 않은 클라이언트로부터 리소스를 숨기기 위해 404 대신 전송.
     */
    @ExceptionHandler({
        NotPresentPaymentException.class,
    })
    @ResponseStatus(FORBIDDEN)
    public ApiUtil.ApiResult<Void> forbidden(Exception e) {
        log.error("e :: {}, message :: {}", e.getClass().getName(), e.getMessage());
        return fail(e, FORBIDDEN);
    }

}
