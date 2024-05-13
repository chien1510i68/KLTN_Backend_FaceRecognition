package com.example.backend_facerecognition.controller;

import com.example.backend_facerecognition.dto.request.facerecognition_request.CreateFaceRecognitionRequest;
import com.example.backend_facerecognition.dto.request.facerecognition_request.UpdateFaceRecognitionRequest;
import com.example.backend_facerecognition.service.facerecognition.FaceRecognitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("face_recognition/")
public class FaceRecognitionController {
    private final FaceRecognitionService faceRecognitionService ;
    @PreAuthorize("hasAnyAuthority('STUDENT','ADMIN' )")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createFaceRecognition (@RequestParam String  qrCodeId ,
                                                    @RequestParam Float longitude,
                                                    @RequestParam Float latitude,
                                                    @RequestParam String userCode,
                                                    @RequestPart MultipartFile image){
        CreateFaceRecognitionRequest request = CreateFaceRecognitionRequest.builder().qrCodeId(qrCodeId).userCode(userCode)
                .longitude(longitude).latitude(latitude).build();
       return  faceRecognitionService.createFaceRecognition(request , image);
    }
    @PutMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN' )")
    public ResponseEntity<?> updateFaceRecognition(@RequestBody UpdateFaceRecognitionRequest request , @PathVariable String id  ){
        return faceRecognitionService.updateFaceRecognition(request , id );
    }
    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN' )")
    public ResponseEntity<?> getFaceRecognition(@PathVariable String id){
        return faceRecognitionService.getFaceRecognition(id) ;
    }
    @PostMapping("test")
    @PreAuthorize("hasAnyAuthority('ADMIN' )")
    public ResponseEntity<?> test(@RequestParam String qrCode ,
                                  @RequestParam String userCode){
        return faceRecognitionService.test(qrCode , userCode);
    }
    @GetMapping("qr/{qrId}")
    @PreAuthorize("hasAnyAuthority('ADMIN' )")
    public ResponseEntity<?> getAllByQr(@PathVariable String qrId) {
        return faceRecognitionService.getFaceRecognitionByQRCodeId(qrId);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN' )")
    public ResponseEntity<?> deleteById (@PathVariable String id){
        return faceRecognitionService.deleteFaceRecognition(id);
    }
}