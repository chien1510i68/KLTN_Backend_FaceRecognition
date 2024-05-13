package com.example.backend_facerecognition.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ClassUser {
    @Id
    private String id ;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User  user ;
    @ManyToOne
    @JoinColumn(name = "class_id")
    @JsonBackReference
    private Classroom  classroom ;


}
