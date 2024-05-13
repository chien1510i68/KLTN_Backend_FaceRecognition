package com.example.backend_facerecognition.dto.request.classroom_request;

import lombok.Data;

@Data
public class DeleteUserInClassroomRequest {
    private String userId ;
    private String classroomId ;
}
