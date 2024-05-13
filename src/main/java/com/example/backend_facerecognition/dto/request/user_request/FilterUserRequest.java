package com.example.backend_facerecognition.dto.request.user_request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterUserRequest {
    private String fullName;
    private String userCode;
    private String className;
    private boolean trained ;
}
