package com.example.backend_facerecognition.controller;

import com.example.backend_facerecognition.dto.request.user_request.*;
import com.example.backend_facerecognition.service.user.UserService;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("user/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService ;


    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('ADMIN' , 'LECTURER')")
    public ResponseEntity<?> getAllUser(){
        return userService.getAllUser();
    }
    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'LECTURER' )")
    public ResponseEntity<?> getById(@PathVariable String id){
        return userService.getByIdUser(id);
    }
    @PostMapping("/createLecturers")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'LECTURER' )")
    public ResponseEntity<?> createAdmin(@RequestBody CreateUserRequest request){
        return userService.createLecturer(request);
    }

    @PostMapping("/file")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'LECTURER' )")
    public ResponseEntity<?> createByFile(@RequestParam("file") MultipartFile file){
        return userService.createUserByExcel(file);
    }

    @PutMapping("")
    @PreAuthorize("hasAnyAuthority('ADMIN' , 'LECTURER')")
    public ResponseEntity<?> updateUser (@RequestBody UpdateUserRequest request){
        return userService.updateUser(request);
    }
    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN' , 'LECTURER')")
    public ResponseEntity<?> deleteById(@PathVariable String id){
        return userService.deleteByIdUser(id);
    }

    @GetMapping("/getLecturers")
    @PreAuthorize("hasAnyAuthority('ADMIN' )")
    public ResponseEntity<?> getLecturers(){
        return userService.getListLecturer();
    }



    @GetMapping("images/{userCode}")
    @PreAuthorize("hasAnyAuthority('ADMIN' ,'STUDENT', 'LECTURER')")
    public ResponseEntity<?> getImagesByUser(@PathVariable String userCode) throws IOException {
        return userService.getImagesByUser(userCode);
    }
    @PostMapping("createImages")
    @PreAuthorize("hasAnyAuthority('ADMIN' ,'STUDENT', 'LECTURER')")
    public ResponseEntity<?> createImageByUser(@RequestBody MultipartFile file) throws IOException {
        return userService.createImages(file);
    }

    @PostMapping("createModel")
    @PreAuthorize("hasAnyAuthority('ADMIN' ,'STUDENT', 'LECTURER')")
    public ResponseEntity<?> createModel(@RequestBody CreateModelsRequest request) throws IOException {
        return userService.createModelByUserCode(request.getUserCodes());
    }

    @PostMapping("filter")
    @PreAuthorize("hasAnyAuthority('ADMIN' , 'LECTURER')")
    public ResponseEntity<?> filterUser(@RequestBody FilterUserRequest request){
        return  userService.filterUser(request);
    }

    @PostMapping("update-image")
    @PreAuthorize("hasAnyAuthority('STUDENT', 'LECTURER' )")
    public ResponseEntity<?> updateImage(@RequestParam MultipartFile file , @RequestParam String fileName) throws IOException {
        return  userService.updateImage(fileName,file);
    }

    @PostMapping("prediction")
    @PreAuthorize("hasAnyAuthority('STUDENT', 'LECTURER' )")
    public ResponseEntity<?> predictUser(@ModelAttribute CreatePredictRequest createPredictRequest) throws IOException {
       return  userService.predictUser(createPredictRequest);
    }

    @PostMapping("trainModel")
    @PreAuthorize("hasAnyAuthority('STUDENT', 'LECTURER' )")
    public ResponseEntity<?> trainingModel(@ModelAttribute CreatePredictRequest request){
        return userService.trainingModel(request);
    }






}
