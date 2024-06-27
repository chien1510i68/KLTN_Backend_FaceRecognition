package com.example.backend_facerecognition.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Classroom {
    @Id
    private String id;
    @Column(name = "quantity_student_in_class")
    private int quantityStudents;

    @Column(name = "class-code")
    private String classCode;
    @Column(name = "study_group")
    private int studyGroup;

    @Column(name = "nameClass")
    private String nameClass ;

    @Column(name = "schoolYear")
    private String schoolYear ;

    @Column(name =  "semester")
    private int semester ;

    @Column(name = "note")
    private String note ;
    @Column(name = "createBy")
    private

    @OneToMany(mappedBy = "classroom" , cascade = CascadeType.ALL)
    @JsonManagedReference
    List<ClassUser> classUsers = new ArrayList<>();


    @OneToMany(mappedBy = "classroom")
    @JsonManagedReference
    private List<FaceRecognitionSection> faceRecognitionSections;


    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User  user ;

}
