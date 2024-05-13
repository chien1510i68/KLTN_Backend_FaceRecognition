package com.example.backend_facerecognition.service.mapper;

import com.example.backend_facerecognition.dto.request.checkins_request.UpdateCheckinsRequest;
import com.example.backend_facerecognition.model.Checkin;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel =  "spring")
public interface CheckinsUpdateMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCheckins(UpdateCheckinsRequest updateCheckinsRequest , @MappingTarget Checkin checkin);
}
