package com.example.backend_facerecognition.config;

import com.example.backend_facerecognition.service.mapper.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
    @Bean
    public UserUpdateMapper userUpdateMapper(){
        return Mappers.getMapper(UserUpdateMapper.class);
    }
    @Bean
    public ClassroomUpdateMapper classroomUpdateMapper(){
        return Mappers.getMapper(ClassroomUpdateMapper.class);
    }
    @Bean
    public FaceRecognitionSectionUpdateMapper faceRecognitionSectionUpdateMapper(){
        return Mappers.getMapper(FaceRecognitionSectionUpdateMapper.class);
    }

    @Bean
    public QRCodeUpdateMapper qrCodeUpdateMapper() {
        return Mappers.getMapper(QRCodeUpdateMapper.class);
    }

    @Bean
    public CheckinsUpdateMapper checkinsUpdateMapper(){
        return Mappers.getMapper(CheckinsUpdateMapper.class);
    }


}
