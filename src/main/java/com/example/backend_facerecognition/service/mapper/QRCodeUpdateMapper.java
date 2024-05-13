package com.example.backend_facerecognition.service.mapper;

import com.example.backend_facerecognition.dto.request.qr_request.UpdateQRCodeRequest;
import com.example.backend_facerecognition.model.QRCode;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface QRCodeUpdateMapper {
    @BeanMapping(nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping( target = "userCreateById" , ignore = true),
            @Mapping(target = "classroomId" , ignore = true)
    })
    void updateQRCode (UpdateQRCodeRequest request , @MappingTarget QRCode qrCode );
}
