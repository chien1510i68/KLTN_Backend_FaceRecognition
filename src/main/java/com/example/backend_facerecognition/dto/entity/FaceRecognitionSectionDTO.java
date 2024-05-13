package com.example.backend_facerecognition.dto.entity;

import com.example.backend_facerecognition.model.Classroom;
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
public class FaceRecognitionSectionDTO {
    private String id;
    private String faceIdentity;
//    private boolean isConfirmed;
    private String note;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss" ,  timezone = "Asia/Ho_Chi_Minh")
    private Timestamp time;
//    private Classroom classroom ;
    private String userName;
    private String phoneNumber ;
    private boolean isConfirmed;
    private  String className ;
    private String dob ;
}
