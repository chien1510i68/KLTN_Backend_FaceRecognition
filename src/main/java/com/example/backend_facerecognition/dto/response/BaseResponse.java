package com.example.backend_facerecognition.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {
    private boolean success ;
    private ErrorResponse error ;


//    private static<T>  BaseItemResponse successData(T data ){
//        BaseItemResponse<T> baseResponse = new BaseItemResponse<>();
//        baseResponse.successData(data);
//        return baseResponse;
//
//    }
//    private static<T> BaseListResponse<T> successListData(List<T> rows , int total ){
//        BaseListResponse<T>  baseListResponse = new BaseListResponse<>();
//        baseListResponse.setResults(rows , total);
//        return baseListResponse;
//    }
//    public static <T> BaseResponse error(int code, String msg) {
//        BaseResponse baseResponse = new BaseResponse();
//        baseResponse.setSuccess(false);
//        ErrorResponse error = new ErrorResponse();
//        error.setMessage(msg);
//        error.setCode(code);
//        baseResponse.setError(error);
//        return baseResponse;
//    }
//
//    public static <T> BaseResponse error(int code, String msg, List<ErrorDetail> errors) {
//        BaseResponse baseResponse = new BaseResponse();
//        baseResponse.setSuccess(false);
//        ErrorResponse error = new ErrorResponse();
//        error.setMessage(msg);
//        error.setCode(code);
//        error.setErrorDetails(errors);
//        baseResponse.setError(error);
//        return baseResponse;
//    }
 }
