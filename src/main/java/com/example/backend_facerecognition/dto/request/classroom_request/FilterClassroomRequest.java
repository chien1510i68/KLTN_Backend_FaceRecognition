package com.example.backend_facerecognition.dto.request.classroom_request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterClassroomRequest {
    private String nameClass;
    private String classCode;
}
