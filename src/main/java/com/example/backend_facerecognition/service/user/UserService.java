package com.example.backend_facerecognition.service.user;


import com.example.backend_facerecognition.dto.entity.UserDTO;
import com.example.backend_facerecognition.dto.request.user_request.CreateUserRequest;
import com.example.backend_facerecognition.dto.request.user_request.FilterUserRequest;
import com.example.backend_facerecognition.dto.request.user_request.UpdateUserRequest;
import com.example.backend_facerecognition.model.User;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface UserService {
    ResponseEntity<?> getAllUser();

    ResponseEntity<?> getByIdUser(String id);

    UserDTO createUser(CreateUserRequest request);


    ResponseEntity<?> deleteByIdUser(String id);

    ResponseEntity<?> updateUser(UpdateUserRequest request );

    List<UserDTO> deleteAllIdUser(List<String> ids);

    ResponseEntity<?> createUserByExcel(MultipartFile file);

    ResponseEntity<?> getImagesByUser(String userCode) throws IOException;

    ResponseEntity<?> createImages(MultipartFile file) throws IOException;

    ResponseEntity<?> createModelByUserCode(List<String> userCodes) throws IOException;

    ResponseEntity<?> filterUser(FilterUserRequest request);
    ResponseEntity<?> filterUsers(FilterUserRequest request , String classroomId);
    ResponseEntity<?> updateImage(String fileName , MultipartFile file) throws IOException;






}
