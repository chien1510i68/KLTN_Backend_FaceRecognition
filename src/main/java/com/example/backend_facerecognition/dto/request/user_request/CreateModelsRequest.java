package com.example.backend_facerecognition.dto.request.user_request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
public class CreateModelsRequest {
    List<String> userCodes ;
}
