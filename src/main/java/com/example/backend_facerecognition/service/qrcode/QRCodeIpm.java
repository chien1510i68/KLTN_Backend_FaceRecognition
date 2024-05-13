package com.example.backend_facerecognition.service.qrcode;

import com.example.backend_facerecognition.constant.Constants;
import com.example.backend_facerecognition.dto.entity.ObjectStatistic;
import com.example.backend_facerecognition.dto.entity.QRCodeDTO;
import com.example.backend_facerecognition.dto.entity.UserDTO;
import com.example.backend_facerecognition.dto.request.qr_request.CreateQRCodeRequest;
import com.example.backend_facerecognition.dto.request.qr_request.FilterQRRequest;
import com.example.backend_facerecognition.dto.request.qr_request.UpdateQRCodeRequest;
import com.example.backend_facerecognition.dto.response.BaseItemResponse;
import com.example.backend_facerecognition.dto.response.BaseListResponse;
import com.example.backend_facerecognition.dto.response.BaseResponseError;
import com.example.backend_facerecognition.dto.response.ErrorResponse;
import com.example.backend_facerecognition.exception.DataNotFoundException;
import com.example.backend_facerecognition.model.*;
import com.example.backend_facerecognition.repository.ClassroomRepository;
import com.example.backend_facerecognition.repository.QRCodeRepository;
import com.example.backend_facerecognition.repository.UserRepository;
import com.example.backend_facerecognition.service.mapper.QRCodeUpdateMapper;
import com.example.backend_facerecognition.utils.DateFormat;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QRCodeIpm implements QRCodeService {
    private final QRCodeRepository qrCodeRepository;
    private final QRCodeUpdateMapper qrCodeUpdateMapper;
    private final UserRepository userRepository;
    private final ClassroomRepository classroomRepository;
    private final ModelMapper mapper;

    @Override
    public ResponseEntity<?> createQRCode(CreateQRCodeRequest request) {
        Optional<User> user = userRepository.findByUserCode(request.getUserCreateById());
        Optional<Classroom> classroom = classroomRepository.findById(request.getClassroomId());

        if (user.isPresent() && classroom.isPresent()) {
            QRCode qrCode = QRCode.builder()
                    .createAt(new Timestamp(new Date().getTime()))
                    .id(UUID.randomUUID().toString())
                    .longitude(request.getLongitude())
                    .latitude(request.getLatitude())
                    .endTime(calculateEndTime( request.getLimitedTime() ,new Timestamp(new Date().getTime())))
                    .isNormal(request.isNormal())
                    .isActive(true)
//                    .limitedTime(request.getLimitedTime())
                    .userCreateById(user.get().getId())
                    .classroomId(request.getClassroomId())
                    .checkins(null).build();
            qrCodeRepository.save(qrCode);
            BaseItemResponse response = new BaseItemResponse();
            response.setSuccess(true);
            QRCodeDTO qrCodeDTO = mapper.map(qrCode, QRCodeDTO.class);
            qrCodeDTO.setCreateBy(userRepository.findById(qrCode.getUserCreateById()).orElseThrow().getFullName());
            qrCodeDTO.setNameClass(classroomRepository.findById(qrCode.getClassroomId()).orElseThrow().getNameClass());
            response.setData(qrCodeDTO);

            return ResponseEntity.ok(response);
        } else {
            BaseResponseError baseResponseError = new BaseResponseError();
            baseResponseError.setFailed(500, "Lớp học hoặc người dùng không tồn tại");
            return ResponseEntity.ok(baseResponseError);
        }

    }



    @Override
    public ResponseEntity<?> updateQRCode(UpdateQRCodeRequest request) {
        Optional<QRCode> qrCode = qrCodeRepository.findById(request.getQrCodeId());
        if (qrCode.isPresent()) {
            qrCode.get().setActive(request.isActive());
            qrCodeUpdateMapper.updateQRCode(request, qrCode.get());
            BaseItemResponse response = new BaseItemResponse();
            response.setSuccess(true);
            response.setData(qrCode.get());
            qrCodeRepository.save(qrCode.get());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.ok(new ErrorResponse(500, Constants.ErrorMessageQRCode.QRCODE_NOT_FOUND, null));
        }
    }

    @Override
    public ResponseEntity<?> getQRCodeById(String id) {
        Optional<QRCode> qrCode = qrCodeRepository.findById(id);
        if (qrCode.isPresent()) {
            BaseItemResponse response = new BaseItemResponse();
            response.setSuccess(true);
            QRCodeDTO qrCodeDTO = mapper.map(qrCode.get(), QRCodeDTO.class);
            qrCodeDTO.setCreateBy(userRepository.findById(qrCode.get().getUserCreateById()).orElseThrow().getFullName());
            qrCodeDTO.setNameClass(classroomRepository.findById(qrCode.get().getClassroomId()).orElseThrow().getNameClass());
            response.setData(qrCodeDTO);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.ok(new ErrorResponse(500, Constants.ErrorMessageQRCode.QRCODE_NOT_FOUND, null));
        }
    }

    @Override
    public ResponseEntity<?> deleteQRCode(String id) {
        Optional<QRCode> qrCode = qrCodeRepository.findById(id);
        if (qrCode.isPresent()) {
            qrCodeRepository.deleteById(id);
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(new ErrorResponse(500, Constants.ErrorMessageQRCode.QRCODE_NOT_FOUND, null));
        }
    }

    @Override
    public ResponseEntity<?> getAllQRCode() {
        List<QRCodeDTO> qrCodeDTOS = qrCodeRepository.findAll().stream().map(qr -> {
            QRCodeDTO qrCodeDTO = mapper.map(qr, QRCodeDTO.class);
            Optional<User> user
                    = userRepository.findById(qr.getUserCreateById());
            Optional<Classroom> classroom = classroomRepository.findById(qr.getClassroomId());
            if (user.isPresent()) {
                qrCodeDTO.setCreateBy(user.get().getFullName());
            }
            if (classroom.isPresent()) {
                qrCodeDTO.setNameClass(classroom.get().getNameClass());
            }

            return qrCodeDTO;

        }).collect(Collectors.toList());
        BaseListResponse baseListResponse = new BaseListResponse();
        baseListResponse.setSuccess(true);
        baseListResponse.setResults(qrCodeDTOS, qrCodeDTOS.size());
        return ResponseEntity.ok(baseListResponse);
    }

    @Override
    public ResponseEntity<?> filterQrCode(FilterQRRequest request) throws ParseException {
        return ResponseEntity.ok(DateFormat.convertStringToTimestamp(request.getTime()));
    }

    @Override
    public ResponseEntity<?> getUsersNotAttended(String qrCodeId) {
        Optional<QRCode> qrCode = qrCodeRepository.findById(qrCodeId);
        if (qrCode.isEmpty()) {
            BaseResponseError baseResponseError = new BaseResponseError();
            baseResponseError.setFailed(500, Constants.ErrorMessageQRCode.QRCODE_NOT_FOUND);
            return ResponseEntity.ok(baseResponseError);
        }
        Optional<Classroom> classroom1 = classroomRepository.findById(qrCode.get().getClassroomId());
        if (classroom1.isEmpty()) {
            BaseResponseError baseResponseError = new BaseResponseError();
            baseResponseError.setFailed(500, Constants.ErrorMessageQRCode.QRCODE_NOT_FOUND);
            return ResponseEntity.ok(baseResponseError);
        }
        Classroom classroom = classroom1.get();
        List<User> userInClassroom = classroom.getClassUsers().stream().map(ClassUser::getUser).collect(Collectors.toList());
        List<User> usersInQr = qrCode.get().getCheckins().stream().map(user ->
        {
            Optional<User> user1 = userRepository.findByUserCode(user.getUserCode());
            if (user1.isPresent()) {
                return user1.get();
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        List<UserDTO> userInClassroomNotInQr = userInClassroom.stream()
                .filter(user -> !usersInQr.contains(user))
                .map(item -> mapper.map(item, UserDTO.class))
                .collect(Collectors.toList());
        BaseListResponse baseListResponse = new BaseListResponse();
        baseListResponse.setSuccess(true);
        baseListResponse.setResults(userInClassroomNotInQr, userInClassroomNotInQr.size());
        return ResponseEntity.ok(baseListResponse);
    }

    @Override
    public ResponseEntity<?> getQrCodeByClassroom(String classroomId) {
        List<QRCodeDTO> qrCodes = qrCodeRepository.findAllByClassroomId(classroomId).stream().map(qr -> {
            QRCodeDTO qrCodeDTO = mapper.map(qr, QRCodeDTO.class);
            Optional<User> user
                    = userRepository.findById(qr.getUserCreateById());
            Optional<Classroom> classroom = classroomRepository.findById(qr.getClassroomId());
            if (user.isPresent()) {
                qrCodeDTO.setCreateBy(user.get().getFullName());
            }
            if (classroom.isPresent()) {
                qrCodeDTO.setNameClass(classroom.get().getNameClass());
            }

            return qrCodeDTO;

        }).sorted(Comparator.comparing(QRCodeDTO::getCreateAt).reversed()).collect(Collectors.toList());
        BaseListResponse baseListResponse = new BaseListResponse();
        baseListResponse.setSuccess(true);
        baseListResponse.setResults(qrCodes , qrCodes.size());

        return ResponseEntity.ok(baseListResponse);
    }
    public static Timestamp calculateEndTime (String limitTimeString , Timestamp startTime){
        LocalTime limitTime = LocalTime.parse(limitTimeString);
        LocalDateTime startDateTime = startTime.toLocalDateTime();
        LocalDateTime endDateTime = startDateTime
                .plusHours(limitTime.getHour())
                .plusMinutes(limitTime.getMinute())
                .plusSeconds(limitTime.getSecond());


        return Timestamp.valueOf(endDateTime) ;
    }


}
