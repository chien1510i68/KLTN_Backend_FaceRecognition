package com.example.backend_facerecognition.controller.auth_controller;

import com.example.backend_facerecognition.dto.request.auth_request.LoginRequest;
import com.example.backend_facerecognition.dto.request.user_request.CreateUserRequest;
import com.example.backend_facerecognition.dto.response.BaseItemResponse;
import com.example.backend_facerecognition.dto.response.LoginResponse;
import com.example.backend_facerecognition.service.auth.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenService ;
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        return authenService.authenticateUser(loginRequest.getUserCode(), loginRequest.getPassword());
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerAccount(@RequestBody CreateUserRequest request){

        return authenService.registerAccount(request);
    }

}
