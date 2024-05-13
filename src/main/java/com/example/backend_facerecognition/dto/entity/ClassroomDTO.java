package com.example.backend_facerecognition.dto.entity;

import lombok.Data;

@Data
public class ClassroomDTO {
    private String id;
    private int quantityStudents;
    private String file;
    private String classCode;
    private String nameClass ;
    private int studyGroup;
    private String schoolYear ;
    private int semester ;
    private String note;
}
