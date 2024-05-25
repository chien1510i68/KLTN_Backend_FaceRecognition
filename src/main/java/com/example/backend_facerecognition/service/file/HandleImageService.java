package com.example.backend_facerecognition.service.file;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.sql.Timestamp;

@Component
public interface HandleImageService {
    ResponseEntity<?> saveFile(byte[] imageData , String classroomId , String userCode, Timestamp timestamp) throws FileNotFoundException;

    String getSignature(String classroomId , Timestamp timestamp , String userCode);
}
