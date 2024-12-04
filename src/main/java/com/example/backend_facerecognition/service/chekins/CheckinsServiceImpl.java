package com.example.backend_facerecognition.service.chekins;

import com.example.backend_facerecognition.constant.Constants;
import com.example.backend_facerecognition.constant.ErrorCodeDefs;
import com.example.backend_facerecognition.dto.entity.AttendedUserDTO;
import com.example.backend_facerecognition.dto.entity.CheckinDTO;
import com.example.backend_facerecognition.dto.entity.CheckinUserDTO;
import com.example.backend_facerecognition.dto.request.checkins_request.CreateCheckinsRequest;
import com.example.backend_facerecognition.dto.request.checkins_request.UpdateCheckinsRequest;
import com.example.backend_facerecognition.dto.response.BaseItemResponse;
import com.example.backend_facerecognition.dto.response.BaseListResponse;
import com.example.backend_facerecognition.dto.response.BaseResponseError;
import com.example.backend_facerecognition.dto.response.ErrorResponse;
import com.example.backend_facerecognition.model.*;
import com.example.backend_facerecognition.repository.*;
import com.example.backend_facerecognition.service.file.HandleImageService;
import com.example.backend_facerecognition.service.mapper.CheckinsUpdateMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheckinsServiceImpl implements CheckinsService {
    private final ClassroomRepository classroomRepository;
    private final QRCodeRepository qrCodeRepository;
    private final CheckinsRepository checkinsRepository;
    private final CheckinsUpdateMapper checkinsUpdateMapper;
    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final HandleImageService handleImageService ;
    private final ClassUserRepository classUserRepository;

    @Override
    public ResponseEntity<?> createCheckins(CreateCheckinsRequest createCheckinsRequest) {

        Optional<QRCode> qrCode = qrCodeRepository.findById(createCheckinsRequest.getQrCode());
        if (qrCode.isPresent()) {
            Optional<Checkin> checkCheckin = checkinsRepository.findByUserCodeAndQrCode(createCheckinsRequest.getUserCode(), qrCode.get());
            if (!checkCheckin.isPresent()) {
                Checkin checkin = Checkin.builder().id(UUID.randomUUID().toString())
                        .time(new Timestamp(new Date().getTime()))
                        .classroomId(qrCode.get().getClassroomId())
                        .userCode(createCheckinsRequest.getUserCode())
                        .userName(createCheckinsRequest.getUserName())
                        .dob(createCheckinsRequest.getDob())
                        .status("Yes")
                        .qrCode(qrCode.get()).build();
                BaseItemResponse response = new BaseItemResponse();
                response.setSuccess(true);
                response.setData(checkinsRepository.saveAndFlush(checkin));
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.ok(new ErrorResponse(ErrorCodeDefs.ERR_INTERNAL_SERVICE, Constants.ErrorMessageCheckins.CHECKIN_IS_EXIST, null));


        } else {
            return ResponseEntity.ok(new ErrorResponse(500, "Lớp học hoặc qrCode không tồn tại", null));
        }


    }


    @Override
    public ResponseEntity<?> updateCheckins(UpdateCheckinsRequest updateCheckinsRequest) {

        Optional<Checkin> checkin = checkinsRepository.findById(updateCheckinsRequest.getCheckinId());
        if (!checkin.isPresent()) {
            return ResponseEntity.ok(new ErrorResponse(ErrorCodeDefs.ERR_SERVER_ERROR, Constants.ErrorMessageCheckins.CHECKIN_NOT_FOUND, null));
        }
        checkinsUpdateMapper.updateCheckins(updateCheckinsRequest, checkin.get());
        if (updateCheckinsRequest.getNote() != null) {
            checkin.get().setNote(updateCheckinsRequest.getNote());
        }
        checkinsRepository.save(checkin.get());
        BaseItemResponse response = new BaseItemResponse();
        response.setSuccess(true);
        response.setData(checkin.get());
        return ResponseEntity.ok(response);

    }

    @Override
    public ResponseEntity<?> getAllCheckin() {
        List<Checkin> checkins = checkinsRepository.findAll();
        BaseListResponse response = new BaseListResponse();
        response.setSuccess(true);
        response.setResults(checkins, checkins.size());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> deleteCheckin(String id) {
        Optional<Checkin> checkin = checkinsRepository.findById(id);
        if (!checkin.isPresent()) {
            return ResponseEntity.ok(new ErrorResponse(ErrorCodeDefs.ERR_SERVER_ERROR, Constants.ErrorMessageCheckins.CHECKIN_NOT_FOUND, null));
        }
        checkinsRepository.delete(checkin.get());
        BaseItemResponse response = new BaseItemResponse();
        response.setSuccess(true);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getCheckinById(String id) {
        Optional<Checkin> checkin = checkinsRepository.findById(id);
        if (!checkin.isPresent()) {
            return ResponseEntity.ok(new ErrorResponse(ErrorCodeDefs.ERR_SERVER_ERROR, Constants.ErrorMessageCheckins.CHECKIN_NOT_FOUND, null));
        }
        BaseItemResponse response = new BaseItemResponse();
        response.setSuccess(true);
        response.setData(checkin.get());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getAllCheckinByQR(String qrId) {
        Optional<QRCode> qrCode = qrCodeRepository.findById(qrId);
        if (qrCode.isEmpty()) {
            BaseResponseError baseResponseError = new BaseResponseError();
            baseResponseError.setFailed(500, Constants.ErrorMessageQRCode.QRCODE_NOT_FOUND);
            return ResponseEntity.ok(baseResponseError);
        }
        Optional<Classroom> classroom = classroomRepository.findById(qrCode.get().getClassroomId());
        if (classroom.isEmpty()) {
            BaseResponseError baseResponseError = new BaseResponseError();
            baseResponseError.setFailed(500, Constants.ErrorMessageClassroomValidation.CLASSROOM_NOT_FOUND);
            return ResponseEntity.ok(baseResponseError);
        }
        List<CheckinDTO> checkins = qrCode.get().getCheckins().stream().map(item -> {
            CheckinDTO checkinDTO = mapper.map(item, CheckinDTO.class);
            checkinDTO.setClassName(classroom.get().getNameClass());
            return checkinDTO;
//            checkinDTO.set
        }).collect(Collectors.toList());
        BaseListResponse baseListResponse = new BaseListResponse();
        baseListResponse.setSuccess(true);
        baseListResponse.setResults(checkins, checkins.size());
        return ResponseEntity.ok(baseListResponse);
    }

    @Override
    public ResponseEntity<?> getCheckinsByClassroomAndUser(String userCode, String classroomId) {
//        List<CheckinUserDTO> checkins = checkinsRepository.findAllByUserCodeAndClassroomId(userId, classroomId)
//                .stream().map(item -> mapper.map(item, CheckinDTO.class)).collect(Collectors.toList());

        Optional<User> user = userRepository.findByUserCode(userCode);
        Optional<Classroom> classroom = classroomRepository.findById(classroomId);
        if (user.isEmpty() || classroom.isEmpty()) {
            BaseResponseError baseResponseError = new BaseResponseError();
            baseResponseError.setFailed(500, Constants.ErrorMessageUserValidation.USER_NOT_FOUND);
            return ResponseEntity.ok(baseResponseError);
        }
        List<Classroom> classrooms = classUserRepository.findAllByUserAndClassroom(user.get() , classroom.get()).stream()
                .map(ClassUser::getClassroom).collect(Collectors.toList());
        List<   AttendedUserDTO> attendedUserDTOList = classrooms.stream().map(classroom1 -> {
            List<QRCode> qrCodes = qrCodeRepository.findAllByClassroomId(classroom1.getId());
            List<CheckinUserDTO> checkinUserDTOS = qrCodes.stream().sorted(Comparator.comparing(QRCode::getCreateAt)).map(qrCode -> {
                CheckinUserDTO checkinUserDTO = new CheckinUserDTO();
                boolean attended = qrCode.getCheckins().stream().anyMatch(checkin -> {
                    boolean isAttended = checkin.getUserCode().equals(userCode);
                    checkinUserDTO.setAttended(isAttended);
                    checkinUserDTO.setTimeAttended(isAttended ? checkin.getTime() : null);

                    return isAttended;
                });
                checkinUserDTO.setTimeCreateQr(qrCode.getCreateAt());
                return checkinUserDTO;


            }).collect(Collectors.toList());
            return AttendedUserDTO.builder().userName(user.get().getFullName())
                    .userCode(user.get().getUserCode())
                    .classCode(classroom1.getClassCode())
                    .nameClass(classroom1.getNameClass())
                    .studyGroup(classroom1.getStudyGroup())
                    .checkinUserDTOS(checkinUserDTOS).build();
        }).toList();
        BaseListResponse response = new BaseListResponse();
        response.setSuccess(true);
        response.setResults(attendedUserDTOList, attendedUserDTOList.size());
        return ResponseEntity.ok(response);

    }

    @Override
    public ResponseEntity<?> getAttendedByUser( String userCode) {

       Optional<User> user = userRepository.findByUserCode(userCode);
        if (user.isEmpty()) {
            BaseResponseError baseResponseError = new BaseResponseError();
            baseResponseError.setFailed(500, Constants.ErrorMessageUserValidation.USER_NOT_FOUND);
            return ResponseEntity.ok(baseResponseError);
        }
        List<ClassUser> classUsers = classUserRepository.findAllByUser(user.get());
        List<Classroom> classrooms = classUsers.stream().map(ClassUser::getClassroom).toList();
        List<   AttendedUserDTO> attendedUserDTOList = classrooms.stream().map(classroom -> {
            List<QRCode> qrCodes = qrCodeRepository.findAllByClassroomId(classroom.getId());
            List<CheckinUserDTO> checkinUserDTOS = qrCodes.stream().sorted(Comparator.comparing(QRCode::getCreateAt)).map(qrCode -> {
                CheckinUserDTO checkinUserDTO = new CheckinUserDTO();
                boolean attended = qrCode.getCheckins().stream().anyMatch(checkin -> {
                    boolean isAttended = checkin.getUserCode().equals(userCode);
                    checkinUserDTO.setAttended(isAttended);
                    checkinUserDTO.setTimeAttended(isAttended ? checkin.getTime() : null);

                    return isAttended;
                });
                checkinUserDTO.setTimeCreateQr(qrCode.getCreateAt());
                return checkinUserDTO;


            }).collect(Collectors.toList());
            return AttendedUserDTO.builder().userName(user.get().getFullName())
                    .userCode(user.get().getUserCode())
                    .classCode(classroom.getClassCode())
                    .nameClass(classroom.getNameClass())
                    .studyGroup(classroom.getStudyGroup())
                    .checkinUserDTOS(checkinUserDTOS).build();
        }).toList();
         BaseListResponse response = new BaseListResponse();
        response.setSuccess(true);
        response.setResults(attendedUserDTOList, attendedUserDTOList.size());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getAllCheckInByClassroomId(String qrCodeId) {
        Optional<QRCode> qrCodeOptional = qrCodeRepository.findById(qrCodeId);
        if (qrCodeOptional.isEmpty()) {
            BaseResponseError baseResponseError = new BaseResponseError();
            baseResponseError.setSuccess(false);
            baseResponseError.setFailed(500, Constants.ErrorMessageQRCode.QRCODE_NOT_FOUND);
            return ResponseEntity.ok().body(baseResponseError);

        }
        String classroomId = qrCodeOptional.get().getClassroomId();
        Optional<Classroom> classroom = classroomRepository.findById(classroomId);
        if (classroom.isEmpty()) {
            BaseResponseError baseResponseError = new BaseResponseError();
            baseResponseError.setSuccess(false);
            baseResponseError.setFailed(500, Constants.ErrorMessageClassroomValidation.CLASSROOM_NOT_FOUND);

            return ResponseEntity.ok().body(baseResponseError);
        }
        List<User> users = classroom.get().getClassUsers().stream().map(ClassUser::getUser).collect(Collectors.toList());
        List<QRCode> qrCodes = qrCodeRepository.findAllByClassroomId(classroomId);
        if (qrCodes.size() == 0) {
            BaseResponseError baseResponseError = new BaseResponseError();
            baseResponseError.setSuccess(false);
            baseResponseError.setFailed(500, Constants.ErrorMessageQRCode.QRCODE_NOT_FOUND);

            return ResponseEntity.ok().body(baseResponseError);
        }
        List<AttendedUserDTO> attendedUserDTOS = new ArrayList<>();
        for (User user : users) {
            AttendedUserDTO userDTO = new AttendedUserDTO();

            userDTO.setUserCode(user.getUserCode());
            userDTO.setUserName(user.getFullName());
            userDTO.setNameClass(user.getClassname());
//            userDTO.setDob(user.getDob());
            List<CheckinUserDTO> checkinUserDTOS = qrCodes.stream().map(qrCode -> {
                CheckinUserDTO checkinUserDTO = new CheckinUserDTO();
                boolean attended = qrCode.getCheckins().stream().anyMatch(checkin -> {
                    boolean isAttended = checkin.getUserCode().equals(user.getUserCode());
                    checkinUserDTO.setAttended(isAttended);
                    checkinUserDTO.setTimeAttended(isAttended ? checkin.getTime() : null);
                    if(isAttended){
                        checkinUserDTO.setSignature(handleImageService.getSignature(classroomId, checkin.getTime() , user.getUserCode()));
                    }
                    return isAttended;
                });
                checkinUserDTO.setTimeCreateQr(qrCode.getCreateAt());
                return checkinUserDTO;
            }).sorted(Comparator.comparing(CheckinUserDTO::getTimeCreateQr)).collect(Collectors.toList());
            userDTO.setCheckinUserDTOS(checkinUserDTOS);
            attendedUserDTOS.add(userDTO);
        }

        BaseItemResponse response = new BaseItemResponse();
        response.setSuccess(true);
        response.setData(attendedUserDTOS);
        return ResponseEntity.ok().body(response);


    }



}
