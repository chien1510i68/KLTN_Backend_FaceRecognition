package com.example.backend_facerecognition.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
public class FaceRecognitionSection {
    @Id
    private String id;
    @Column(name = "face_identity")
    private String faceIdentity;

    @Column(name = "")
    private boolean isConfirmed;

    @Column(name = "note")
    private String note;

    @Column(name = "time")
    private Timestamp time;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;


    @ManyToOne
    @JoinColumn(name = "classroom_id")
    @JsonBackReference
    private Classroom classroom;



    @ManyToOne
    @JoinColumn(name = "qr_id")
    @JsonBackReference
    private QRCode qrCode;

}
