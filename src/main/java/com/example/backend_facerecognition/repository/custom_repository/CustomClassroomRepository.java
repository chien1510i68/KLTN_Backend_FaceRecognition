package com.example.backend_facerecognition.repository.custom_repository;

import com.example.backend_facerecognition.dto.request.classroom_request.FilterClassroomRequest;
import com.example.backend_facerecognition.model.Classroom;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomClassroomRepository {
    public static Specification<Classroom> filterClassRoom(FilterClassroomRequest request){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.hasText(request.getNameClass())){
                predicates.add(criteriaBuilder.like(root.get("nameClass") , "%" +request.getNameClass() +"%"));
            }
            if(StringUtils.hasText(request.getClassCode())){
                predicates.add(criteriaBuilder.like(root.get("classCode") ,"%"+ request.getClassCode() + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
