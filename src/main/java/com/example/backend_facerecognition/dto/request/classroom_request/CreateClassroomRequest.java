package com.example.backend_facerecognition.dto.request.classroom_request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data

public class CreateClassroomRequest {
    private String id;
    private int quantityStudents;
    private MultipartFile file ;
    private String className;
    private int studyGroup;
    private String classCode ;
    private String schoolYear ;
    private int semester ;
    private String note ;

}
