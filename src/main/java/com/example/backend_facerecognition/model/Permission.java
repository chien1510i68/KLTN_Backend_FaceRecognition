package com.example.backend_facerecognition.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    @Column(length = 100)
    private String id;

    @Column(length = 100)
    private String name;

    @Column(length = 100)
    private String type;

    @ManyToMany(mappedBy = "permissions")
    private Collection<Role> roles;
}
