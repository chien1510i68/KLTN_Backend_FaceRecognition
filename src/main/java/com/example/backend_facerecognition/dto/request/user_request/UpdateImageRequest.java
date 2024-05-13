package com.example.backend_facerecognition.dto.request.user_request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateImageRequest {
    private String fileName ;
    private MultipartFile file ;
}
