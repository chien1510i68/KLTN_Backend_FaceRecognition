package com.example.backend_facerecognition.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseListResponse<T> extends BaseResponse {
    private int code;
    private DataList data;

    public void setResults(List<T> rows , int total ){
        if(rows!= null){
            super.setSuccess(true);
        data = new DataList();
        data.setTotal(total);
        data.setItems(rows);
        }
    }
}
