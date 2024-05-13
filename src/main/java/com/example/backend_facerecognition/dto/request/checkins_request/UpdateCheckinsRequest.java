package com.example.backend_facerecognition.dto.request.checkins_request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateCheckinsRequest {

    private String userName ;
    private String userCode ;
    @NotBlank(message = "Không được để trống id ")
    private String checkinId ;

    private String note ;

}
