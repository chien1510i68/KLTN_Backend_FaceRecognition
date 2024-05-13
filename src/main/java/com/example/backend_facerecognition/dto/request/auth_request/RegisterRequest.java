package com.example.backend_facerecognition.dto.request.auth_request;

import com.example.backend_facerecognition.model.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String userCode;
    private String address ;
    private String phoneNumber ;
    private String fullName;
    private String dob;
    private String classname;
    private Role role;
}
