package com.example.backend_facerecognition.service.file;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
@Component
public interface HandleImageService {
    ResponseEntity<?> saveFile(byte[] imageData , String classroomId , String userCode) throws FileNotFoundException;

    ResponseEntity<?> getSignature(String classroomId , String date , String userCode);
}
