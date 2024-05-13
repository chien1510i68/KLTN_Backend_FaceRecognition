package com.example.backend_facerecognition.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String jwt ;
    private String userName ;
    private String userCode ;
    private List<String> roles ;
}
