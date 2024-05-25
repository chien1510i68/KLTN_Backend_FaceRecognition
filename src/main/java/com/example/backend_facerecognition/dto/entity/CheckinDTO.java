package com.example.backend_facerecognition.dto.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class CheckinDTO {
    private  String id ;
    private String className ;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss" ,  timezone = "Asia/Ho_Chi_Minh")
    private Timestamp time;
    private String userName ;
    private String userCode ;
    private String status ;
    private String dob ;
    private String note ;
    private Double distance ;
}
