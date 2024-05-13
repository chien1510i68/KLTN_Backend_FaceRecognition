package com.example.backend_facerecognition.service.classroom;

import com.example.backend_facerecognition.common.ReadDataFromExcel;
import com.example.backend_facerecognition.constant.Constants;
import com.example.backend_facerecognition.dto.entity.ClassroomDTO;
import com.example.backend_facerecognition.dto.entity.ObjectStatistic;
import com.example.backend_facerecognition.dto.entity.UserDTO;
import com.example.backend_facerecognition.dto.request.classroom_request.CreateClassroomRequest;
import com.example.backend_facerecognition.dto.request.classroom_request.FilterClassroomRequest;
import com.example.backend_facerecognition.dto.request.classroom_request.UpdateClassroomRequest;
import com.example.backend_facerecognition.dto.request.user_request.CreateUserRequest;
import com.example.backend_facerecognition.dto.response.BaseItemResponse;
import com.example.backend_facerecognition.dto.response.BaseListResponse;
import com.example.backend_facerecognition.dto.response.BaseResponseError;
import com.example.backend_facerecognition.dto.response.ErrorResponse;
import com.example.backend_facerecognition.exception.DataNotFoundException;
import com.example.backend_facerecognition.model.*;
import com.example.backend_facerecognition.repository.*;
import com.example.backend_facerecognition.repository.custom_repository.CustomClassroomRepository;
import com.example.backend_facerecognition.service.mapper.ClassroomUpdateMapper;
import com.example.backend_facerecognition.service.mapper.UserRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassroomServiceIplm implements ClassroomService {
    private final ClassroomRepository classroomRepository;
    private final ModelMapper mapper;
    private final ClassUserRepository classUserRepository;
    private final ClassroomUpdateMapper classroomUpdateMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final QRCodeRepository qrCodeRepository;

    @Override

    public ResponseEntity<?> createClassroom(CreateClassroomRequest request) {

        boolean isExistSchoolAndSemester = classroomRepository.existsBySchoolYearAndSemesterAndClassCode(request.getSchoolYear(), request.getSemester(), request.getClassCode());
        if (isExistSchoolAndSemester) {
            BaseResponseError baseResponseError = new BaseResponseError();
            baseResponseError.setSuccess(false);
            baseResponseError.setFailed(500, Constants.ErrorMessageClassroomValidation.SEMESTER_AND_SCHOOLYEAR_EXISTED);
            return ResponseEntity.ok(baseResponseError);
        }
        Classroom classroom = Classroom.builder().id(UUID.randomUUID().toString()).classCode(request.getClassCode()).studyGroup(request.getStudyGroup())
                .nameClass(request.getClassName())
                .schoolYear(request.getSchoolYear())
                .semester(request.getSemester())
                .note(request.getNote())
                .quantityStudents(0).build();
        classroomRepository.save(classroom);

        BaseItemResponse response = new BaseItemResponse();
        response.setSuccess(true);
        response.setData(mapper.map(classroom, ClassroomDTO.class));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> updateClassroom(UpdateClassroomRequest request) {

        Optional<Classroom> classroom = classroomRepository.findById(request.getId());
        if (classroom.isEmpty()) {

            BaseResponseError baseResponseError = new BaseResponseError();
            baseResponseError.setFailed(500, Constants.ErrorMessageClassroomValidation.CLASSROOM_NOT_FOUND);
            return ResponseEntity.ok(baseResponseError);
        }

        classroomUpdateMapper.updateClassroom(request, classroom.get());

        if(request.getNote()!= null){
            classroom.get().setNote(request.getNote());
        }


        BaseItemResponse response = new BaseItemResponse();
        response.setSuccess(true);
        response.setData(mapper.map(classroomRepository.save(classroom.get()), ClassroomDTO.class));
        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<?> createClassroomByFile(MultipartFile file) {
        return null;
    }

    @Override
    public ResponseEntity<?> deleteClassroom(String id) {
        Optional<Classroom> classroom = classroomRepository.findById(id);
        if (classroom.isPresent()) {
            BaseItemResponse response = new BaseItemResponse();
            response.successData(null);
            List<ClassUser> classUsers = classroom.get().getClassUsers();
            for (ClassUser classUser : classUsers) {
                classUserRepository.delete(classUser);
            }
            classroomRepository.delete(classroom.get());
            return ResponseEntity.ok(response);
        } else {
            BaseResponseError baseResponseError = new BaseResponseError();
            baseResponseError.setFailed(500, Constants.ErrorMessageClassroomValidation.CLASSROOM_NOT_FOUND);
            return ResponseEntity.ok(baseResponseError);
        }


    }

    @Override
    public ResponseEntity<?> showClassroom(String id) {
        Classroom classroom = classroomRepository.findById(id).orElseThrow(() -> new DataNotFoundException(Constants.ErrorMessageClassroomValidation.CLASSROOM_NOT_FOUND));
        List<User> users = new ArrayList<>();
        for (ClassUser user : classroom.getClassUsers()) {
            users.add(user.getUser());
        }

        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<?> updateListStudent(MultipartFile file, String id) {
        Optional<Classroom> classroom = classroomRepository.findById(id);
        Role role = roleRepository.findById("STUDENT").orElseThrow();
        if (!classroom.isPresent()) {
            return ResponseEntity.ok(ErrorResponse.builder().message(Constants.ErrorMessageClassroomValidation.CLASSROOM_NOT_FOUND).build());
        } else {
            ReadDataFromExcel<User> readDataFromExcel = new ReadDataFromExcel<>(User.class, new UserRowMapper());
            List<User> users = readDataFromExcel.readFromExcel(file);
            List<User> userInDB = userRepository.findAll();
//            List<User> usersInClassroom = classroom.get().getClassUsers().stream()
//                    .map(ClassUser::getUser)
//                    .filter(Objects::nonNull)
//                    .collect(Collectors.toList());
            List<User> usersNotInDB = users.stream().
                    filter(user -> userInDB.stream().noneMatch(u -> u.getUserCode().equals(user.getUserCode()))).collect(Collectors.toList());
            if (usersNotInDB.size() > 0) {
                List<User> saveUserInDB = usersNotInDB.stream().map(item -> {
                    item.setRole(role);
                    item.setPassword(passwordEncoder.encode("1111"));
                    item.setId(UUID.randomUUID().toString());
                    return item;
                }).collect(Collectors.toList());
                userRepository.saveAll(saveUserInDB);
                List<ClassUser> classUsers = saveUserInDB.stream().map(user -> {
                    return ClassUser.builder().id(UUID.randomUUID().toString()).user(user).classroom(classroom.get()).build();
                }).collect(Collectors.toList());
                classUserRepository.saveAll(classUsers);
                classroom.get().setQuantityStudents(classUsers.size());
                classroomRepository.save(classroom.get());
//                classroomRepository.save(classroom.get());
                BaseListResponse baseListResponse = new BaseListResponse();
                baseListResponse.setSuccess(true);
                baseListResponse.setResults(classUsers, classUsers.size());
                return ResponseEntity.ok(baseListResponse);
            } else {
                List<ClassUser> classUsers = users.stream().map(user -> {
                            User optionalUser = userRepository.findByUserCode(user.getUserCode()).orElseThrow();
                            Optional<ClassUser> classUser = classUserRepository.findByUserAndClassroom(optionalUser, classroom.get());
                            if (!classUser.isPresent()) {
                                return ClassUser.builder().id(UUID.randomUUID().toString()).user(optionalUser).classroom(classroom.get()).build();
                            }
                            return null;
                        })
                        .filter(Objects::nonNull).collect(Collectors.toList());
                classUserRepository.saveAll(classUsers);
                classroom.get().setQuantityStudents(classUsers.size());
                classroomRepository.save(classroom.get());
//                classroomRepository.save(classroom.get());
                BaseListResponse baseListResponse = new BaseListResponse();
                baseListResponse.setSuccess(true);
                baseListResponse.setResults(classUsers, classUsers.size());
                return ResponseEntity.ok(baseListResponse);
            }

        }
//        return null;


    }

    @Override
    public ResponseEntity<?> getAllClassroom() {
        BaseListResponse response = new BaseListResponse();
        response.setSuccess(true);
        response.setResults(classroomRepository.findAll().stream().map(item -> mapper.map(item, ClassroomDTO.class)).collect(Collectors.toList()), classroomRepository.findAll().size());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> filterClassroom(FilterClassroomRequest request) {
        Specification<Classroom> specification = CustomClassroomRepository.filterClassRoom(request);
        List<ClassroomDTO> classroomDTOS = classroomRepository.findAll(specification).stream().map(item -> mapper.map(item, ClassroomDTO.class)).collect(Collectors.toList());
        return ResponseEntity.ok(classroomDTOS);
    }

    @Override
    public ResponseEntity<?> getStudentInClassroom(String classroomId) {
        Optional<Classroom> classroom = classroomRepository.findById(classroomId);
        if (classroom.isPresent()) {
            List<UserDTO> userDTOS = classroom.get().getClassUsers().stream()
                    .map(ClassUser::getUser)
                    .map(user -> mapper.map(user, UserDTO.class))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            BaseListResponse baseListResponse = new BaseListResponse();
            baseListResponse.setSuccess(true);
            baseListResponse.setResults(userDTOS, userDTOS.size());
            return ResponseEntity.ok(baseListResponse);
        }
        return null;
    }

    @Override
    public ResponseEntity<?> deleteUserInClassroom(String userId, String classroomId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Classroom> classroom = classroomRepository.findById(classroomId);
        if (user.isPresent() && classroom.isPresent()) {
            Optional<ClassUser> classUser = classUserRepository.findByUserAndClassroom(user.get(), classroom.get());
            if (classUser.isPresent()) {
                classUserRepository.delete(classUser.get());
                classroom.get().setQuantityStudents(classroom.get().getClassUsers().size());
                classroomRepository.save(classroom.get());
            }
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    @Override
    public ResponseEntity<?> getClassroomByUser(String userCode) {
        User user = userRepository.findUserByUserCode(userCode);
        if (user == null) {
            BaseResponseError baseResponseError = new BaseResponseError();
            baseResponseError.setFailed(500, Constants.ErrorMessageUserValidation.USER_NOT_FOUND);
            return ResponseEntity.ok(baseResponseError);
        }
        List<ClassroomDTO> classroomDTOS = user.getClassUsers().stream()
                .map(ClassUser::getClassroom)
                .map(classroom -> mapper.map(classroom, ClassroomDTO.class))
                .collect(Collectors.toList());
        BaseListResponse baseListResponse = new BaseListResponse();
        baseListResponse.setSuccess(true);
        baseListResponse.setResults(classroomDTOS, classroomDTOS.size());
        return ResponseEntity.ok(baseListResponse);
    }

    @Override
    public ResponseEntity<?> addUserInClassroom(CreateUserRequest request, String classroomId) {
        ClassUser classUser = new ClassUser();

        Optional<Role> roleOptional = roleRepository.findById("STUDENT");
        Optional<Classroom> classroom = classroomRepository.findById(classroomId);
        if (roleOptional.isEmpty() || classroom.isEmpty()) {
            BaseResponseError baseResponseError = new BaseResponseError();
            baseResponseError.setFailed(500, "Không tìm thấy vai trò hoặc lớp học");
        }
        classUser.setClassroom(classroom.get());
        Optional<User> user1 = userRepository.findByUserCode(request.getUserCode());
        if (user1.isPresent()) {
            classUser.setUser(user1.get());
            classroom.get().setQuantityStudents(classroom.get().getQuantityStudents() + 1);
        } else {
            User user = User.builder()
                    .fullName(request.getFullName())
                    .dob(request.getDob())
                    .id(UUID.randomUUID().toString())
                    .classname(request.getClassname())
                    .userCode(request.getUserCode())
                    .address(null)
                    .phoneNumber(request.getPhoneNumber())
                    .role(roleOptional.get())
                    .password(passwordEncoder.encode(request.getPassword())).build();
            classUser.setUser(user);
            classroom.get().setQuantityStudents(classroom.get().getQuantityStudents() + 1);
        }
        classroomRepository.save(classroom.get());
        classUserRepository.save(classUser);
        BaseItemResponse response = new BaseItemResponse();
        response.setSuccess(true);
        response.setData(classUser);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> statisticQrByClassroom(String qrcodeId) {
        Optional<QRCode> qrCodeOptional = qrCodeRepository.findById(qrcodeId);
        if (qrCodeOptional.isEmpty()) {
            BaseResponseError baseResponseError = new BaseResponseError();
            baseResponseError.setSuccess(false);
            baseResponseError.setFailed(500, Constants.ErrorMessageQRCode.QRCODE_NOT_FOUND);
            return ResponseEntity.ok(baseResponseError);
        }
        Optional<Classroom> classroom = classroomRepository.findById(qrCodeOptional.get().getClassroomId());
        if (qrCodeOptional.isEmpty() || classroom.isEmpty()) {
            BaseResponseError baseResponseError = new BaseResponseError();
            baseResponseError.setSuccess(false);
            baseResponseError.setFailed(500, Constants.ErrorMessageClassroomValidation.CLASSROOM_NOT_FOUND);
            return ResponseEntity.ok(baseResponseError);
        }

        List<QRCode> qrCodes = qrCodeRepository.findAllByClassroomId(qrCodeOptional.get().getClassroomId());
        List<ObjectStatistic> objectStatistics = qrCodes.stream().flatMap(qrCode -> {
            ObjectStatistic statistic1 = ObjectStatistic.builder()
                    .idQr(qrCode.getId())
                    .type("attended")
                    .quantity(qrCode.getCheckins().size())
                    .time(qrCode.getCreateAt()).build();

            ObjectStatistic statistic2 = ObjectStatistic.builder().type("not-attended")
                    .idQr(qrCode.getId())
                    .quantity(classroom.get().getQuantityStudents() - qrCode.getCheckins().size())
                    .time(qrCode.getCreateAt()).build();
            return Stream.of(statistic1, statistic2);

        }).sorted(Comparator.comparing(ObjectStatistic::getTime).reversed()).collect(Collectors.toList());
        BaseListResponse baseListResponse = new BaseListResponse();
        baseListResponse.setSuccess(true);
        baseListResponse.setResults(objectStatistics, objectStatistics.size());


        return ResponseEntity.ok(baseListResponse);
    }

    @Override
    public ResponseEntity<?> addStudentInClassroom(CreateUserRequest request, String classroomId) {
        Optional<Role> role = roleRepository.findById("STUDENT");
        Optional<Classroom> classroom = classroomRepository.findById(classroomId);

        boolean userExisted = userRepository.existsByUserCode(request.getUserCode());
        if (role.isEmpty()) {
            BaseResponseError baseResponseError = new BaseResponseError();
            baseResponseError.setFailed(500, Constants.ErrorMessageRoleValidation.ROLE_NOT_FOUND);
            return ResponseEntity.ok(baseResponseError);
        }
        if (classroom.isEmpty()) {
            BaseResponseError baseResponseError = new BaseResponseError();
            baseResponseError.setFailed(500, Constants.ErrorMessageClassroomValidation.CLASSROOM_NOT_FOUND);
            return ResponseEntity.ok(baseResponseError);
        }
        ClassUser classUser = new ClassUser();
        classUser.setId(UUID.randomUUID().toString());
        classUser.setClassroom(classroom.get());

        if (userExisted) {
            User user1 = userRepository.findUserByUserCode(request.getUserCode());
            Optional<ClassUser> classUser1 = classUserRepository.findByUserAndClassroom(user1, classroom.get());
            if (classUser1.isPresent()) {
                BaseResponseError baseResponseError = new BaseResponseError();
                baseResponseError.setFailed(500, "Người dùng đã tồn tại trong lớp học");
                return ResponseEntity.ok(baseResponseError);
            } else {

                classUser.setUser(user1);
                classroom.get().setQuantityStudents(classroom.get().getClassUsers().size()+1);
            }

        } else {
            User user = User.builder().id(UUID.randomUUID().toString())
                    .fullName(request.getFullName())
                    .dob(request.getDob())
                    .classname(request.getClassname())
                    .userCode(request.getUserCode())
                    .address(request.getAddress())
                    .phoneNumber(request.getPhoneNumber())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(role.get()).build();
            userRepository.save(user);
            classUser.setUser(user);
            classroom.get().setQuantityStudents(classroom.get().getClassUsers().size()+1);



        }

//        classroomRepository.save(classroom.get());
        classUserRepository.save(classUser);
        BaseItemResponse response = new BaseItemResponse();
        response.setSuccess(true);
        response.setData(classUser);
        return ResponseEntity.ok(response);
    }
}
