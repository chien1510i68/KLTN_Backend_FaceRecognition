package com.example.backend_facerecognition.service.classroom;

import com.example.backend_facerecognition.dto.request.classroom_request.CreateClassroomRequest;
import com.example.backend_facerecognition.dto.request.classroom_request.FilterClassroomRequest;
import com.example.backend_facerecognition.dto.request.classroom_request.UpdateClassroomRequest;
import com.example.backend_facerecognition.dto.request.user_request.CreateUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ClassroomService {
    ResponseEntity<?> createClassroom(CreateClassroomRequest request  );
    ResponseEntity<?> updateClassroom(UpdateClassroomRequest request );
    ResponseEntity<?> createClassroomByFile(MultipartFile file);

    ResponseEntity<?> deleteClassroom(String id);
    ResponseEntity<?> showClassroom(String id);

    ResponseEntity<?> addUserByFileExcel(MultipartFile file , String id);

    ResponseEntity<?>  getAllClassroom () ;

    ResponseEntity<?> getClassroomsByUserCode(String userCode) ;
    ResponseEntity<?> filterClassroom (FilterClassroomRequest request);
    ResponseEntity<?> getStudentInClassroom (String classroomId) ;
    ResponseEntity<?> deleteUserInClassroom (String userId , String classroomId);
    ResponseEntity<?> getClassroomByUser(String userCode) ;

    ResponseEntity<?> addUserInClassroom(CreateUserRequest request , String classroomId);

    ResponseEntity<?> statisticQrByClassroom (String qrcodeId);
    ResponseEntity<?> addStudentInClassroom (CreateUserRequest request , String classroomId);

}
