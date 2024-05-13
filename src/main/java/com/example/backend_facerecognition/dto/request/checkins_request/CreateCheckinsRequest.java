package com.example.backend_facerecognition.dto.request.checkins_request;

import com.example.backend_facerecognition.model.QRCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCheckinsRequest {
    private String userName ;
    private String userCode ;
    private String qrCode;
    private String dob ;
}
