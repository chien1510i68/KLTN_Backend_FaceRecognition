package com.example.backend_facerecognition.service.qrcode;

import com.example.backend_facerecognition.dto.request.qr_request.CreateQRCodeRequest;
import com.example.backend_facerecognition.dto.request.qr_request.FilterQRRequest;
import com.example.backend_facerecognition.dto.request.qr_request.UpdateQRCodeRequest;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;


public interface QRCodeService {
    ResponseEntity<?> createQRCode(CreateQRCodeRequest request);
    ResponseEntity<?> updateQRCode(UpdateQRCodeRequest request  );

    ResponseEntity<?> getQRCodeById(String id);

    ResponseEntity<?> deleteQRCode(String id );


    ResponseEntity<?> getAllQRCode();

    ResponseEntity<?> filterQrCode (FilterQRRequest request) throws ParseException;

    ResponseEntity<?> getUsersNotAttended(String qrCodeId);

    ResponseEntity<?> getQrCodeByClassroom (String classroomId);



}
