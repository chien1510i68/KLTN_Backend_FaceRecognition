package com.example.backend_facerecognition.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
@Builder
public class User {
    @Id
    private String id;

    @Column(name = "full_name")
    private String fullName;
    @Column(name = "date_of_birth")
    private String dob;
    @Column(name = "classname")
    private String classname;
    @Column(name = "user_code")
    private String userCode ;
    @Column(name = "address")
    private String address ;
    @Column(name = "phoneNumber")
    private String phoneNumber ;
    @Column(name = "trained")
    private Boolean trained ;
    @Size(max = 100)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @JsonBackReference
    private Role role;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    List<ClassUser> classUsers = new ArrayList<>();


    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    List<Classroom> classrooms = new ArrayList<>();


    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<FaceRecognitionSection> recognitionSections ;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (role != null) {
            role.getPermissions().forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getId()))); // lay den quyen
            authorities.add(new SimpleGrantedAuthority(role.getId())); //lay den vai tro
        }
        return authorities;
    }





}
