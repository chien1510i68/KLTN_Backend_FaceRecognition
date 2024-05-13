package com.example.backend_facerecognition.exception;

import com.example.backend_facerecognition.constant.ErrorCodeDefs;
import com.example.backend_facerecognition.dto.response.BaseResponse;
import com.example.backend_facerecognition.dto.response.BaseResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class HandlerException {


    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponseError methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        BaseResponseError baseResponse = BaseResponseError.builder()
                .success(false)
                .error(processFielError(fieldErrors)).build();
        return baseResponse;
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public BaseResponseError httpMediaTypeNotAcceptableException(HttpMessageNotReadableException ex) {
        return BaseResponseError.builder().success(false).error(
                BaseResponseError.Error.builder().code(ErrorCodeDefs.ERR_VALIDATION)
                        .message(ErrorCodeDefs.getMessage(ErrorCodeDefs.ERR_VALIDATION)).build()
        ).build();
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public BaseResponseError methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return BaseResponseError.builder().success(false).error(
                BaseResponseError.Error.builder().code(ErrorCodeDefs.ERR_BAD_REQUEST)
                        .message(ErrorCodeDefs.getMessage(ErrorCodeDefs.ERR_BAD_REQUEST)).build()
        ).build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @ExceptionHandler(AlertBadException.class)
    public BaseResponseError alertBadException(AlertBadException ex) {
        return BaseResponseError.builder().success(false).error(
                BaseResponseError.Error.builder().code(ErrorCodeDefs.ERR_BAD_REQUEST)
                        .message(ex.getMessage()).build()
        ).build();
    }


    private BaseResponseError.Error processFielError(List<FieldError> fieldErrors) {
        BaseResponseError.Error error = BaseResponseError.Error.builder().code(ErrorCodeDefs.ERR_VALIDATION)
                .message(ErrorCodeDefs.getMessage(ErrorCodeDefs.ERR_VALIDATION)).build();
        List<BaseResponseError.ErrorDetail> detailErrorList = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            BaseResponseError.ErrorDetail detailError = BaseResponseError.ErrorDetail.builder().message(fieldError.getDefaultMessage())
                    .id(fieldError.getField()).build();
            detailErrorList.add(detailError);
        }
        error.setErrorDetailList(detailErrorList);
        return error;
    }


}


