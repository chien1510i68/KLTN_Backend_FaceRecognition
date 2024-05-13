package com.example.backend_facerecognition.service.mapper;

import com.example.backend_facerecognition.dto.request.facerecognition_request.UpdateFaceRecognitionRequest;
import com.example.backend_facerecognition.model.FaceRecognitionSection;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface FaceRecognitionSectionUpdateMapper {
    @BeanMapping(nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE)
    void updateFaceRecognition(UpdateFaceRecognitionRequest request , @MappingTarget FaceRecognitionSection  faceRecognitionSection);

}
