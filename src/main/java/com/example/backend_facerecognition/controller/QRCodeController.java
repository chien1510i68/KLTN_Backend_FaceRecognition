package com.example.backend_facerecognition.controller;

import com.example.backend_facerecognition.dto.request.qr_request.CreateQRCodeRequest;
import com.example.backend_facerecognition.dto.request.qr_request.FilterQRRequest;
import com.example.backend_facerecognition.dto.request.qr_request.UpdateQRCodeRequest;
import com.example.backend_facerecognition.service.qrcode.QRCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("qr/")
@RequiredArgsConstructor
public class QRCodeController {
    private final QRCodeService qrCodeService;


    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'LECTURER' )")
    public ResponseEntity<?> getQRCodeById(@PathVariable String id) {
        return qrCodeService.getQRCodeById(id);
    }
    @GetMapping("filter/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'LECTURER' )")
    public ResponseEntity<?> getUsersNotAttended(@PathVariable String id){
        return qrCodeService.getUsersNotAttended(id);
    }


    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN' , 'LECTURER')")
    public ResponseEntity<?> getAllQRCode() {
        return qrCodeService.getAllQRCode();
    }

    @PostMapping("filter")
    @PreAuthorize("hasAnyAuthority('ADMIN' , 'LECTURER')")
    public ResponseEntity<?> filterQrcode(@RequestBody FilterQRRequest request) throws ParseException {
        return qrCodeService.filterQrCode(request);
    }
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'LECTURER' )")
    public ResponseEntity<?> createQRCode(@RequestBody CreateQRCodeRequest request) {
        return qrCodeService.createQRCode(request);
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'LECTURER' )")
    public ResponseEntity<?> updateQRCode(@RequestBody UpdateQRCodeRequest request) {
        return qrCodeService.updateQRCode(request);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'LECTURER' )")
    public ResponseEntity<?> deleteQrCode(@PathVariable String id) {
        return qrCodeService.deleteQRCode(id);
    }

    @GetMapping("/quantity/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN' ,'STUDENT', 'LECTURER')")
    public ResponseEntity<?> getQRCodesByClassroomId(@PathVariable String id){
        return qrCodeService.getQrCodeByClassroom(id);
    }


}
