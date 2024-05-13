package com.example.backend_facerecognition.dto.request.user_request;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String address;
    private String phoneNumber ;
    private String userCode ;
    private String fullName;
    private String dob;
    private String classname;
    private String id ;
    private String password ;

}
