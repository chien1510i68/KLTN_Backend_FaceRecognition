package com.example.backend_facerecognition.dto.request.user_request;

import lombok.Data;

@Data
public class UpdateUserRequest {

    private String phoneNumber ;
    private String userCode ;
    private String dob;
    private String classname;
    private String address;


}
