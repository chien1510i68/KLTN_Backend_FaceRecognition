package com.example.backend_facerecognition.dto.request.facerecognition_request;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Data
@Builder
public class CreateFaceRecognitionRequest {
    private String qrCodeId ;
    private Float longitude;
    private Float latitude ;
    private String userCode ;
}
