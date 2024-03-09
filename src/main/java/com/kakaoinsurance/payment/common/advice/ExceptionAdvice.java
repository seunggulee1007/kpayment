package com.kakaoinsurance.payment.common.advice;

import com.kakaoinsurance.payment.common.advice.exceptions.*;
import com.kakaoinsurance.payment.common.utils.ApiUtil;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.kakaoinsurance.payment.common.utils.ApiUtil.fail;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ApiUtil.ApiResult<Void> defaultException(Exception e) {
        e.printStackTrace();
        return fail(e, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
        AlreadyPresentAccountException.class,
        NotMatchedPasswordException.class,
        NotValidMemberException.class,
        InvalidInstallmentMonthException.class,
        ConstraintViolationException.class
    })
    @ResponseStatus(BAD_REQUEST)
    public ApiUtil.ApiResult<Void> badRequest(Exception e) {
        e.printStackTrace();
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
        return fail(e, FORBIDDEN);
    }

}
