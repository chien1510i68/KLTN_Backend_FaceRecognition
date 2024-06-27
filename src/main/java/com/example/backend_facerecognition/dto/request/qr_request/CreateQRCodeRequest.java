package com.example.backend_facerecognition.dto.request.qr_request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateQRCodeRequest {
    @NotBlank(message = "Longitude must not be blank")
    private Double longitude;
    @NotBlank(message = "latitude must not be blank")
    private Double latitude;
    @NotBlank(message = "limitedTime must not be blank")
    private String limitedTime;
    private String userCreateById;
    @NotBlank(message = "classroomId must not be blank")
    private String classroomId;
    @NotBlank(message = "isNormal must not be blank")
    private boolean isNormal;
}
