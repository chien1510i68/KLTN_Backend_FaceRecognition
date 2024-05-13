package com.example.backend_facerecognition.dto.request.qr_request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterQRRequest {
    private String time ;
    private String classCode ;
}
