package com.example.backend_facerecognition.service.chekins;

import com.example.backend_facerecognition.dto.request.checkins_request.CreateCheckinsRequest;
import com.example.backend_facerecognition.dto.request.checkins_request.UpdateCheckinsRequest;
import org.springframework.http.ResponseEntity;

public interface CheckinsService {
    ResponseEntity<?> createCheckins(CreateCheckinsRequest createCheckinsRequest);
    ResponseEntity<?> updateCheckins(UpdateCheckinsRequest updateCheckinsRequest) ;
    ResponseEntity<?> getAllCheckin();
    ResponseEntity<?> deleteCheckin(String id ) ;

    ResponseEntity<?> getCheckinById(String id ) ;

    ResponseEntity<?> getAllCheckinByQR(String qrId);

    ResponseEntity<?> getCheckinsByClassroomAndUser(String userId , String classroomId);

    ResponseEntity<?> getAttendedByUser(String classroomId , String userCode );
    ResponseEntity<?> getAllCheckInByClassroomId(String qrCodeId);
}
