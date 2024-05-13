package com.example.backend_facerecognition.common;

import lombok.Data;

@Data
public class IdentifyPatternConstant {
    public static final String USER_ID_PATTERN = "^[0-9]+$";
    public static final String ADMIN_ID_PATTERN = "^[a-zA-Z][a-zA-Z0-9]*$";
    public static final String CLASS_ID_PATTERN = "^K\\d+[A-Z]+$";
}
