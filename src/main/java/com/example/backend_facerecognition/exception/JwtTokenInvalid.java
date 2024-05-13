package com.example.backend_facerecognition.exception;

import lombok.Data;

@Data
public class JwtTokenInvalid extends RuntimeException{
    public JwtTokenInvalid(String message) {
        super(message);
    }
}
