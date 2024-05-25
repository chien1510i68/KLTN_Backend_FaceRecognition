package com.example.backend_facerecognition.dto.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckinUserDTO {
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss" ,  timezone = "Asia/Ho_Chi_Minh")
    private Timestamp timeAttended ;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss" ,  timezone = "Asia/Ho_Chi_Minh")
    private Timestamp timeCreateQr ;

    private boolean attended ;
    private String signature ;
}
