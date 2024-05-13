package com.example.backend_facerecognition.controller;

import com.example.backend_facerecognition.dto.request.classroom_request.CreateClassroomRequest;
import com.example.backend_facerecognition.dto.request.classroom_request.DeleteUserInClassroomRequest;
import com.example.backend_facerecognition.dto.request.classroom_request.FilterClassroomRequest;
import com.example.backend_facerecognition.dto.request.classroom_request.UpdateClassroomRequest;
import com.example.backend_facerecognition.dto.request.user_request.CreateUserRequest;
import com.example.backend_facerecognition.service.classroom.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("classroom/")
public class ClassroomController {
    private final ClassroomService classroomService;

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN' , 'STUDENT')")
    public ResponseEntity<?> getClassroomById(@PathVariable String id) {
        return classroomService.showClassroom(id);
    }
    @GetMapping("students/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN' )")
    public  ResponseEntity<?> getListUserInClassroom(@PathVariable String id){
        return classroomService.getStudentInClassroom(id);
    }


    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN' )")
    public ResponseEntity<?> getAllClassroom() {
        return classroomService.getAllClassroom();
    }
    @GetMapping("user/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN' ,'STUDENT')")
    public ResponseEntity<?> getClassroomsByUser(@PathVariable String id){
        return classroomService.getClassroomByUser(id);
    }
    @GetMapping("/statistic/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN' )")
    public ResponseEntity<?> statisticClassroom (@PathVariable String id){
        return classroomService.statisticQrByClassroom(id);
    }
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN' )")
    public ResponseEntity<?> createClassroom(@RequestBody CreateClassroomRequest request) {
        return classroomService.createClassroom(request);
    }
    @PostMapping("filter")
    @PreAuthorize("hasAnyAuthority('ADMIN' )")
    public ResponseEntity<?> filterClassroom(@RequestBody FilterClassroomRequest request) {
        return classroomService.filterClassroom(request);
    }
    @PostMapping("user")
    @PreAuthorize("hasAnyAuthority('ADMIN' )")
    public ResponseEntity<?> deleteUserInClassroom (@RequestBody DeleteUserInClassroomRequest request){
        return classroomService.deleteUserInClassroom(request.getUserId(), request.getClassroomId());
    }
    @PutMapping("user/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN' )")
    public ResponseEntity<?> addUserInClass(@RequestBody CreateUserRequest request , @PathVariable String id ){
//        return ResponseEntity.ok(request.getPassword());
        return classroomService.addStudentInClassroom(request , id );
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN' )")
    public ResponseEntity<?> updateClassroom(@RequestBody MultipartFile file, @PathVariable String id) {
        return classroomService.updateListStudent(file, id);
    }
    @PutMapping("update")
    @PreAuthorize("hasAnyAuthority('ADMIN' )")
    public ResponseEntity<?> updateClassroom (@RequestBody UpdateClassroomRequest request){
        return classroomService.updateClassroom(request);
    }
    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN' )")
    public ResponseEntity<?> deleteClassroom(@PathVariable String id) {
        return classroomService.deleteClassroom(id);
    }


}
