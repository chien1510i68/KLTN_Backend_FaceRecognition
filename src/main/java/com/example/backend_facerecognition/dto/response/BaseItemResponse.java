package com.example.backend_facerecognition.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class BaseItemResponse<T> extends BaseResponse{
    private T data ;
    public void successData(T data ){
        setSuccess(true);
        this.data = data;
    }
}
