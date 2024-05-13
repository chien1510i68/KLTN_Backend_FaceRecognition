package com.example.backend_facerecognition.service.mapper;

import com.example.backend_facerecognition.dto.request.user_request.CreateUserRequest;
import com.example.backend_facerecognition.dto.request.user_request.UpdateUserRequest;
import com.example.backend_facerecognition.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserUpdateMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping( target = "password" , ignore = true),

    })
    void updateUserFromDto(UpdateUserRequest request , @MappingTarget User user);
}
