package com.example.backend_facerecognition.dto.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QRCodeDTO {
    private String id;
    private double longitude;
    private double latitude ;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss" ,  timezone = "Asia/Ho_Chi_Minh")
    private Timestamp createAt ;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss" ,  timezone = "Asia/Ho_Chi_Minh")
    private Timestamp endTime ;
    private String createBy ;
    private String nameClass ;
    private boolean isActive ;
    private  boolean isNormal ;
}
