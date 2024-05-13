package com.example.backend_facerecognition.dto.request.qr_request;

import lombok.Data;

@Data
public class UpdateQRCodeRequest {
    private Float longitude;
    private Float latitude ;
    private int limitedTime  ;
    private String userCreateById ;
    private String classroomId ;
    private String qrCodeId ;
    private boolean active;
    private boolean isNormal;
}
