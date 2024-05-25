package com.example.backend_facerecognition.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendedUserDTO {
    private String userName ;
    private String userCode ;
    private String classroom ;
    private String attended;
    private String dob;

    List<CheckinUserDTO> checkinUserDTOS ;
}
