package com.example.backend_facerecognition.repository;

import com.example.backend_facerecognition.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    Optional<User> findByUserCode(String userCode);
    User findUserByUserCode(String userCode);
    boolean existsByUserCode(String userCode );
}
