package com.example.backend_facerecognition.dto.entity;

import com.example.backend_facerecognition.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String id;
//    private String key ;
    private String address ;
    private String phoneNumber ;
    private String fullName;
    private String dob;
    private String userCode ;
    private String classname;

}
