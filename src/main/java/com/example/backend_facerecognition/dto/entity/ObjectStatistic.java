package com.example.backend_facerecognition.dto.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ObjectStatistic {
    private String idQr ;
    private String type ;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss" ,  timezone = "Asia/Ho_Chi_Minh")
    private Timestamp time ;
    private Integer quantity ;
}
