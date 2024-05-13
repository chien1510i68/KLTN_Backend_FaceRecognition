package com.example.backend_facerecognition.repository;

import com.example.backend_facerecognition.model.QRCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QRCodeRepository extends JpaRepository <QRCode , String >{
    List<QRCode> findAllByClassroomId(String classroomId);
}
