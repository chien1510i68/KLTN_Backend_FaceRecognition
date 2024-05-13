package com.example.backend_facerecognition.dto.request.facerecognition_request;

import lombok.Data;

@Data
public class UpdateFaceRecognitionRequest {
    private String faceIdentity;
    private boolean confirmed;
    private String note;
}
