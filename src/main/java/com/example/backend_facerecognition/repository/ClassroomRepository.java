package com.example.backend_facerecognition.repository;

import com.example.backend_facerecognition.model.Classroom;
import com.example.backend_facerecognition.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom , String> , JpaSpecificationExecutor<Classroom> {
    boolean existsBySchoolYearAndSemesterAndClassCode(String  schoolYear , int semester , String classCode);

}
