package com.example.backend_facerecognition.service.facerecognition;

import com.example.backend_facerecognition.dto.request.facerecognition_request.CreateFaceRecognitionRequest;
import com.example.backend_facerecognition.dto.request.facerecognition_request.UpdateFaceRecognitionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FaceRecognitionService {
    ResponseEntity<?> createFaceRecognition(CreateFaceRecognitionRequest request , MultipartFile image , MultipartFile signature);

    ResponseEntity<?> updateFaceRecognition(UpdateFaceRecognitionRequest  request , String id);

    ResponseEntity<?> deleteFaceRecognition(String id);
    ResponseEntity<?> getFaceRecognition(String id );
    ResponseEntity<?> test(String qrCode , String userCode );
    ResponseEntity<?> getFaceRecognitionByQRCodeId(String qrCodeID);

}
