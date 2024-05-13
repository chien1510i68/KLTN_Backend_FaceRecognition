package com.example.backend_facerecognition.repository;

import com.example.backend_facerecognition.model.Checkin;
import com.example.backend_facerecognition.model.QRCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CheckinsRepository extends JpaRepository<Checkin , String > {
    Optional<Checkin> findByQrCode(QRCode qrCode);
    Optional<Checkin> findByUserCodeAndQrCode(String userCode , QRCode qrCode);
    List<Checkin> findAllByUserCodeAndClassroomId(String userCode , String classroomId);
    Optional<Checkin> findByUserCode(String userCode);
}
