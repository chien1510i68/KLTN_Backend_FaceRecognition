package com.example.backend_facerecognition.dto.request.auth_request;

import lombok.Data;

@Data
public class LoginRequest {
    private String userCode ;
    private String password ;
}
