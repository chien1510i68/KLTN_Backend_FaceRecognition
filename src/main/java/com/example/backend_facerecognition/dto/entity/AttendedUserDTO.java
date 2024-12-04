package com.example.backend_facerecognition.dto.entity;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendedUserDTO {
    private String userName;
    private String userCode;
    private String classCode;
    private int studyGroup;
    private String nameClass;
    List<CheckinUserDTO> checkinUserDTOS;
}
