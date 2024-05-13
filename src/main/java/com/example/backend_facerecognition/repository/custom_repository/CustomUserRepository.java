package com.example.backend_facerecognition.repository.custom_repository;

import com.example.backend_facerecognition.dto.request.user_request.FilterUserRequest;
import com.example.backend_facerecognition.model.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomUserRepository {
    public static Specification<User> filterUser(FilterUserRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(request.getFullName())) {
                predicates.add(criteriaBuilder.like(root.get("fullName"), "%" + request.getFullName() + "%" ));
            }
            if (StringUtils.hasText(request.getClassName())) {
                predicates.add(criteriaBuilder.like(root.get("classname"), "%" + request.getClassName()+"%"  ));
            }
            if (StringUtils.hasText(request.getUserCode())) {
                predicates.add(criteriaBuilder.like(root.get("userCode"), "%" + request.getUserCode() + "%"));
            }
            if (request.isTrained()) {
                predicates.add(criteriaBuilder.equal(root.get("trained"), true));
            }
            query.orderBy(criteriaBuilder.desc(root.get("userCode")));
          return  criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
