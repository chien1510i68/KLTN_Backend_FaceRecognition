package com.example.backend_facerecognition.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AlertBadException extends RuntimeException {
    private String msg;
    private Integer code;
}
