package com.example.backend_facerecognition.controller;

import com.example.backend_facerecognition.dto.request.checkins_request.CreateCheckinsRequest;
import com.example.backend_facerecognition.dto.request.checkins_request.FilterUserAndClassroomRequest;
import com.example.backend_facerecognition.dto.request.checkins_request.UpdateCheckinsRequest;
import com.example.backend_facerecognition.service.chekins.CheckinsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("checkins/")
public class CheckinsController {
    private final CheckinsService checkinsService ;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> createCheckin(@RequestBody CreateCheckinsRequest createCheckinsRequest){
        return checkinsService.createCheckins(createCheckinsRequest);
    }
    @PutMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> updateCheckin(@RequestBody UpdateCheckinsRequest updateCheckinsRequest){
        return checkinsService.updateCheckins(updateCheckinsRequest);
    }
    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> deleteCheckin(@PathVariable String id){
        return checkinsService.deleteCheckin(id);
    }
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> getAllCheckin(){
        return checkinsService.getAllCheckin();
    }
    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> getCheckinById(@PathVariable String id ){
        return checkinsService.getCheckinById(id);
    }
    @GetMapping("qr/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> getCheckinsByQR (@PathVariable String id){
        return checkinsService.getAllCheckinByQR(id);
    }

    @PostMapping("classroom")
    @PreAuthorize("hasAnyAuthority('ADMIN','STUDENT')")
    public ResponseEntity<?> getCheckinsByClassroomAndUser(@RequestBody FilterUserAndClassroomRequest request){
        return  checkinsService.getCheckinsByClassroomAndUser(request.getUserCode(), request.getClassroomId());
    }
    @PostMapping("user")
    @PreAuthorize("hasAnyAuthority('STUDENT' , 'ADMIN')")
    public ResponseEntity<?> getHistoryCheckinsByUser(@RequestBody FilterUserAndClassroomRequest request){
        return checkinsService.getAttendedByUser(request.getClassroomId(), request.getUserCode());
    }
}
