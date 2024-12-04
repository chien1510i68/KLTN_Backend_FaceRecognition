package com.example.backend_facerecognition.repository;

import com.example.backend_facerecognition.model.ClassUser;
import com.example.backend_facerecognition.model.Classroom;
import com.example.backend_facerecognition.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassUserRepository extends JpaRepository<ClassUser , String> {
    Optional<ClassUser> findByUserAndClassroom(User user , Classroom classroom);
    List<ClassUser> findAllByUserAndClassroom(User user ,Classroom classroom);
    List<ClassUser> findAllByUser(User user);

//    List<ClassUser> findAllByClassroom(Classroom classroom);
}
