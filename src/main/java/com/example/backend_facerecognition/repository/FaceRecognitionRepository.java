package com.example.backend_facerecognition.repository;

import com.example.backend_facerecognition.model.FaceRecognitionSection;
import com.example.backend_facerecognition.model.QRCode;
import com.example.backend_facerecognition.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FaceRecognitionRepository extends JpaRepository<FaceRecognitionSection , String > {
    List<FaceRecognitionSection> findAllByUserAndQrCode(User user , QRCode qrCode);
    List<FaceRecognitionSection> findAllByQrCode(QRCode qrCode);
    
}
