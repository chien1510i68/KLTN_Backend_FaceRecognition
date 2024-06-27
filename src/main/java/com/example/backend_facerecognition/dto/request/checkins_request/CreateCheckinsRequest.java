package com.example.backend_facerecognition.dto.request.checkins_request;

import com.example.backend_facerecognition.model.QRCode;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCheckinsRequest {
    @NotBlank(message = "Username must not be blank")
    private String userName ;
    @NotBlank(message = "userCode must not be blank")
    private String userCode ;
    @NotBlank(message = "qrCode must not be blank")
    private String qrCode;
    @NotBlank(message = "date of birth must not be blank")
    private String dob ;
}
