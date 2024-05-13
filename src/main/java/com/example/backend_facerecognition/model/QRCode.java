package com.example.backend_facerecognition.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table
@Entity
public class QRCode {
    @Id
    private String id;
    @Column(name = "longitude")
    private double longitude;

    @Column(name = "latitude")
    private double latitude ;

//    @Column(name = "limitedTime")
//    private String limitedTime  ;

    @Column(name = "createAt")
    private Timestamp createAt ;

    @Column(name = "endTime")
    private Timestamp endTime ;

    @Column(name = "userCreateById")
    private String userCreateById ;

    @Column(name = "classroomId")
    private String classroomId ;

    @Column(name = "active")
    private boolean isActive;
    @Column(name = "normal")
    private boolean isNormal;

    @OneToMany(mappedBy = "qrCode")
    @JsonManagedReference
    private List<Checkin> checkins ;


    @OneToMany(mappedBy = "qrCode")
    @JsonManagedReference
    private List<FaceRecognitionSection> recognitionSections;
}
