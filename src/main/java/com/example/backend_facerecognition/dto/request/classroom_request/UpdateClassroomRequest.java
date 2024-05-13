package com.example.backend_facerecognition.dto.request.classroom_request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data

public class UpdateClassroomRequest {
    private String id;
    private int quantityStudents;
    private String classCode;
    private int studyGroup;
    private String nameClass ;
    private Integer semester ;
    private String schoolYear ;
    private String note ;
}
