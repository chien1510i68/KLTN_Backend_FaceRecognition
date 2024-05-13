package com.example.backend_facerecognition.service.auth;

import com.example.backend_facerecognition.dto.entity.UserDTO;
import com.example.backend_facerecognition.dto.request.user_request.CreateUserRequest;
import com.example.backend_facerecognition.dto.response.LoginResponse;
import com.example.backend_facerecognition.model.User;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<?> authenticateUser(String username, String password);

    //    LoginResponse verifyExpiration(String token) throws IOException;
    ResponseEntity<?> registerAccount(CreateUserRequest request);
}
