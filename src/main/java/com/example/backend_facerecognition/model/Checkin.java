package com.example.backend_facerecognition.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table
@Entity
public class Checkin {
    @Id
    private  String id ;

    @Column
    private String classroomId ;

    @Column
    private Timestamp time ;

    @Column
    private String userName ;

    @Column
    private String userCode ;

    @Column
    private String status ;

    @Column
    private String dob ;
    @Column
    private String note ;


    @ManyToOne
    @JoinColumn(name = "qr_id")
    @JsonBackReference
    private QRCode qrCode;





}
