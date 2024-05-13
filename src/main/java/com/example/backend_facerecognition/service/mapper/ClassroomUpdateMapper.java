package com.example.backend_facerecognition.service.mapper;

import com.example.backend_facerecognition.dto.request.classroom_request.UpdateClassroomRequest;
import com.example.backend_facerecognition.model.Classroom;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ClassroomUpdateMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateClassroom(UpdateClassroomRequest request , @MappingTarget Classroom classroom);


}
