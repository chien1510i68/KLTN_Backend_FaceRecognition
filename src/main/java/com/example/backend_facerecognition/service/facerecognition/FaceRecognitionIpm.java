package com.example.backend_facerecognition.service.facerecognition;

import com.example.backend_facerecognition.constant.Constants;
import com.example.backend_facerecognition.constant.ErrorCodeDefs;
import com.example.backend_facerecognition.dto.entity.CheckinDTO;
import com.example.backend_facerecognition.dto.entity.FaceRecognitionSectionDTO;
import com.example.backend_facerecognition.dto.request.checkins_request.CreateCheckinsRequest;
import com.example.backend_facerecognition.dto.request.facerecognition_request.CreateFaceRecognitionRequest;
import com.example.backend_facerecognition.dto.request.facerecognition_request.UpdateFaceRecognitionRequest;
import com.example.backend_facerecognition.dto.response.BaseItemResponse;
import com.example.backend_facerecognition.dto.response.ErrorResponse;
import com.example.backend_facerecognition.exception.DataNotFoundException;
import com.example.backend_facerecognition.model.Checkin;
import com.example.backend_facerecognition.model.FaceRecognitionSection;
import com.example.backend_facerecognition.model.QRCode;
import com.example.backend_facerecognition.model.User;
import com.example.backend_facerecognition.repository.*;
import com.example.backend_facerecognition.service.chekins.CheckinsService;
import com.example.backend_facerecognition.service.file.HandleImageIplm;
import com.example.backend_facerecognition.service.mapper.FaceRecognitionSectionUpdateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FaceRecognitionIpm implements FaceRecognitionService {

    private final FaceRecognitionRepository faceRecognitionRepository;
    private final FaceRecognitionSectionUpdateMapper faceRecognitionSectionUpdateMapper;
    private final QRCodeRepository qrCodeRepository;
    private final CheckinsService checkinsService;
    private final FaceRecogClient faceRecogClient;
    private final ModelMapper mapper;
    private final CheckinsRepository checkinsRepository;
    private final HandleImageIplm handleImageIplm;


    @Override
    public ResponseEntity<?> createFaceRecognition(CreateFaceRecognitionRequest request, MultipartFile image, MultipartFile signature) {
        Optional<QRCode> qrCode = qrCodeRepository.findById(request.getQrCodeId());
        Checkin checkin = new Checkin();
        if (qrCode.isEmpty()) {
            return createErrorResponse(Constants.ErrorMessageQRCode.QRCODE_NOT_FOUND);
        }
        Optional<Checkin> checkin1 = checkinsRepository.findByUserCodeAndQrCode(request.getUserCode(), qrCode.get());
        if (checkin1.isPresent()) {
            return createErrorResponse(Constants.ErrorMessageCheckins.CHECKIN_IS_EXIST);
        }
        if (image == null && !qrCode.get().isNormal()) {
            return createErrorResponse(Constants.ErrorMessageFaceRecognition.NOT_EXITS_INPUT_IMAGE);
        }
        QRCode qrCode1 = qrCode.get();
        User user = faceRecogClient.checkUserInClassroom(qrCode1, request.getUserCode());
        if (user == null) {
            return createErrorResponse(Constants.ErrorMessageFaceRecognition.USER_NOT_IN_CLASSROOM);
        }
        if (request.getLongitude() == null || request.getLatitude() == null) {
            return createErrorResponse(ErrorCodeDefs.getMessage(ErrorCodeDefs.ERR_VALIDATION));
        }
        if (!qrCode1.isActive()) {
            return createErrorResponse(Constants.ErrorMessageQRCode.QRCODE_NOT_ACTIVE);
        }
        double distance = calDistance(qrCode1.getLatitude(), qrCode1.getLongitude(), request.getLatitude(), request.getLongitude());
        if (distance > 100000) {
            return createErrorResponse(Constants.ErrorMessageFaceRecognition.OUTSIDE_OF_ALLOWABLE_RANGE + distance);
        }
        try {
            if (qrCode1.isNormal()) {
                checkin = Checkin.builder()
                        .time(new Timestamp(new Date().getTime()))
                        .classroomId(qrCode1.getClassroomId())
                        .id(UUID.randomUUID().toString())
                        .note(null).userName(user.getFullName())
                        .userCode(request.getUserCode())
                        .distance( distance)
                        .dob(user.getDob())
                        .qrCode(qrCode1).status("signature").build();
                handleImageIplm.saveFile(signature.getBytes(), qrCode1.getClassroomId(), request.getUserCode(), new Timestamp(new Date().getTime()));
                checkinsRepository.save(checkin);

            } else {
                Integer resultPredict = (faceRecogClient.predictFace(image, request.getUserCode()));

                if (resultPredict > 80 && resultPredict <= 100) {
                    checkin = Checkin.builder()
                            .time(new Timestamp(new Date().getTime()))
                            .classroomId(qrCode1.getClassroomId())
                            .id(UUID.randomUUID().toString())
                            .distance(distance)
                            .note(null).userName(user.getFullName()).userCode(request.getUserCode()).dob(user.getDob())
                            .qrCode(qrCode1).status("face").build();
                    checkinsRepository.save(checkin);
                } else if (resultPredict == 500) {
                    return createErrorResponse("Không thể tìm thấy model nhận dạng khuôn mặt " );
                } else if (resultPredict == 502) {
                    return createErrorResponse("Không phát hiện ra khuôn mặt" );
                } else {
                    return createErrorResponse("Có lỗi khi tải mô hình nhận dạng" );

                }


            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BaseItemResponse response = new BaseItemResponse();
        response.setSuccess(true);
        response.successData(mapper.map(checkin, CheckinDTO.class));
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<BaseItemResponse> createErrorResponse(String errorMessage) {
        BaseItemResponse response = new BaseItemResponse();
        response.setSuccess(false);
        response.setError(new ErrorResponse(500, errorMessage, null));
        ;
        return ResponseEntity.ok(response);
    }

    public static double calDistance(double lat1, double lon1, double lat2, double lon2) {
        float R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return c * R * 1000;
    }


    @Override
    public ResponseEntity<?> updateFaceRecognition(UpdateFaceRecognitionRequest request, String id) {
        Optional<FaceRecognitionSection> faceRecognitionSection = faceRecognitionRepository.findById(id);
        if (!faceRecognitionSection.isPresent()) {
            ErrorResponse response = new ErrorResponse();
            response.setCode(500);
            response.setMessage(Constants.ErrorMessageFaceRecognition.FACE_RECOGNITION_NOT_FOUND);
            return ResponseEntity.ok(response);
        }
        faceRecognitionSectionUpdateMapper.updateFaceRecognition(request, faceRecognitionSection.get());
        if (request.isConfirmed() == true) {
            CreateCheckinsRequest checkinsRequest = CreateCheckinsRequest.builder()
                    .userName(faceRecognitionSection.get().getUser().getFullName())
                    .userCode(faceRecognitionSection.get().getUser().getUserCode())
                    .dob(faceRecognitionSection.get().getUser().getDob())
//                    .distance()
                    .qrCode(faceRecognitionSection.get().getQrCode().getId()).build();
            return checkinsService.createCheckins(checkinsRequest);
        }
        BaseItemResponse response = new BaseItemResponse();
        response.setSuccess(true);
        response.setData(faceRecognitionRepository.saveAndFlush(faceRecognitionSection.get()));
        return ResponseEntity.ok(response);

    }

    @Override
    public ResponseEntity<?> deleteFaceRecognition(String id) {
        Optional<FaceRecognitionSection> faceRecognitionSection = faceRecognitionRepository.findById(id);
        if (!faceRecognitionSection.isPresent()) {
            ErrorResponse response = new ErrorResponse();
            response.setCode(500);
            response.setMessage(Constants.ErrorMessageFaceRecognition.FACE_RECOGNITION_NOT_FOUND);
            return ResponseEntity.ok(response);
        } else {
            BaseItemResponse response = new BaseItemResponse();
            response.setSuccess(true);
            response.setData(id);
            faceRecognitionRepository.deleteById(id);
            return ResponseEntity.ok(response);
        }
    }

    @Override
    public ResponseEntity<?> getFaceRecognition(String id) {
        Optional<FaceRecognitionSection> faceRecognitionSection = faceRecognitionRepository.findById(id);
        if (!faceRecognitionSection.isPresent()) {
            ErrorResponse response = new ErrorResponse();
            response.setCode(500);
            response.setMessage(Constants.ErrorMessageFaceRecognition.FACE_RECOGNITION_NOT_FOUND);
            return ResponseEntity.ok(response);
        } else {
            BaseItemResponse response = new BaseItemResponse();
            response.setSuccess(true);
            response.setData(faceRecognitionSection.get());
            return ResponseEntity.ok(response);
        }
    }

    @Override
    public ResponseEntity<?> test(String qrCode, String userCode) {
        Optional<QRCode> qrCode1 = qrCodeRepository.findById(qrCode);
        if (!qrCode1.isPresent()) {
            return ResponseEntity.ok(false);
        }
        User user = faceRecogClient.checkUserInClassroom(qrCode1.get(), userCode);

        return ResponseEntity.ok(faceRecogClient.checkTime(qrCode1.get(), new Timestamp(new Date().getTime())));


    }

    @Override
    public ResponseEntity<?> getFaceRecognitionByQRCodeId(String qrCodeID) {
        QRCode qrCode = qrCodeRepository.findById(qrCodeID).orElseThrow(() -> new DataNotFoundException(Constants.ErrorMessageQRCode.QRCODE_NOT_FOUND));
        List<FaceRecognitionSection> faceRecognitionSection = faceRecognitionRepository.findAllByQrCode(qrCode);


        List<FaceRecognitionSectionDTO> faceRecognitionSectionDTOS = faceRecognitionSection.stream().map(item -> {
            FaceRecognitionSectionDTO sectionDTO = mapper.map(item, FaceRecognitionSectionDTO.class);
            sectionDTO.setUserName(item.getUser().getFullName());
            sectionDTO.setPhoneNumber(item.getUser().getPhoneNumber());
            sectionDTO.setClassName(item.getUser().getClassname());
            sectionDTO.setDob(item.getUser().getDob());

            return sectionDTO;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        BaseItemResponse response = new BaseItemResponse();
        response.setSuccess(true);
        response.setData(faceRecognitionSectionDTOS);
        return ResponseEntity.ok(response);
    }
}


