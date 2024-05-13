package com.example.backend_facerecognition.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImagesUserDTO {

        private String file_name;
        private String encoded_image ;
    }



