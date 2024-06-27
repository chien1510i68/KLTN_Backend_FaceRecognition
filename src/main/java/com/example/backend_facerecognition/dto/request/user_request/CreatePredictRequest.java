package com.example.backend_facerecognition.dto.request.user_request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePredictRequest {
    MultipartFile image ;
    String userCode ;

}
