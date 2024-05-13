package com.example.backend_facerecognition.dto.request.qr_request;

import lombok.Data;

@Data
public class CreateQRCodeRequest {
    private Double longitude;
    private Double latitude;
    private String limitedTime;
    private String userCreateById;
    private String classroomId;
    private boolean isNormal;
}
