package com.example.backend_facerecognition.dto.request.checkins_request;

import lombok.Data;

@Data
public class FilterUserAndClassroomRequest {
    private String userCode ;
    private String classroomId ;
}
