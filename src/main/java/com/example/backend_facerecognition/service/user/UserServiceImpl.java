package com.example.backend_facerecognition.service.user;

import com.example.backend_facerecognition.common.ReadDataFromExcel;
import com.example.backend_facerecognition.constant.Constants;
import com.example.backend_facerecognition.constant.ErrorCodeDefs;
import com.example.backend_facerecognition.dto.entity.UserDTO;
import com.example.backend_facerecognition.dto.request.user_request.CreateUserRequest;
import com.example.backend_facerecognition.dto.request.user_request.FilterUserRequest;
import com.example.backend_facerecognition.dto.request.user_request.UpdateUserRequest;
import com.example.backend_facerecognition.dto.response.BaseItemResponse;
import com.example.backend_facerecognition.dto.response.BaseListResponse;
import com.example.backend_facerecognition.dto.response.BaseResponseError;
import com.example.backend_facerecognition.dto.response.ErrorResponse;
import com.example.backend_facerecognition.exception.MyCustomException;
import com.example.backend_facerecognition.model.ClassUser;
import com.example.backend_facerecognition.model.Classroom;
import com.example.backend_facerecognition.model.Role;
import com.example.backend_facerecognition.model.User;
import com.example.backend_facerecognition.repository.ClassroomRepository;
import com.example.backend_facerecognition.repository.RoleRepository;
import com.example.backend_facerecognition.repository.UserRepository;
import com.example.backend_facerecognition.repository.custom_repository.CustomUserRepository;
import com.example.backend_facerecognition.service.facerecognition.FaceRecogClient;
import com.example.backend_facerecognition.service.mapper.UserRowMapper;
import com.example.backend_facerecognition.service.mapper.UserUpdateMapper;
import com.example.backend_facerecognition.service.python.BaseServicePython;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;




@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ClassroomRepository classroomRepository ;
    private final PasswordEncoder encoder;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserUpdateMapper userUpdateMapper;
    private final ModelMapper mapper ;


    @Override
    public ResponseEntity<?> getAllUser() {
        List<UserDTO> userDTOS = userRepository.findAll().stream().map(
                user -> modelMapper.map(user, UserDTO.class)
        ).toList();
        BaseListResponse response = new BaseListResponse();
        response.setSuccess(true);
        response.setResults(userDTOS, userDTOS.size());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getByIdUser(String id) {
        Optional<User> user = userRepository.findById(id);
        BaseItemResponse response = new BaseItemResponse();
        if (user.isPresent()) {
            response.successData(modelMapper.map(user.get(), UserDTO.class));
            return ResponseEntity.ok(response);
        } else {
            throw new RuntimeException("ID người dùng không tồn tại trong hệ thống!");
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> createLecturer(CreateUserRequest request) {

            Optional<User> user1 = userRepository.findByUserCode(request.getUserCode());
            if (user1.isPresent()){
                BaseResponseError baseResponseError = new BaseResponseError();
                baseResponseError.setSuccess(false);
                baseResponseError.setFailed(500 , Constants.ErrorMessageUserValidation.USER_HAVE_EXISTED);
                return ResponseEntity.ok(baseResponseError);
            }
            Optional<Role> roleOptional = roleRepository.findById("LECTURER");
            User user = User.builder()
                    .fullName(request.getFullName())
                    .dob(request.getDob())
                    .id(UUID.randomUUID().toString())
                    .classname(request.getClassname())
                    .userCode(request.getUserCode())
                    .address(null)
                    .phoneNumber(request.getPhoneNumber())
                    .role(roleOptional.get())
                    .password(encoder.encode(request.getPassword())).build();
            BaseItemResponse response = new BaseItemResponse();
            response.setSuccess(true);
            response.setData(modelMapper.map(userRepository.saveAndFlush(user), UserDTO.class));
            return ResponseEntity.ok().body(response);

    }


    @Override
    public ResponseEntity<?> getListLecturer() {
        List<UserDTO> userDTOS = userRepository.findAll().stream().filter(user -> user.getRole().getName().equals("LECTURER"))
                .map(user -> mapper.map(user , UserDTO.class))
                .collect(Collectors.toList());
        BaseListResponse response = new BaseListResponse();
        response.setSuccess(true);
        response.setResults(userDTOS , userDTOS.size());
         return ResponseEntity.ok(response);
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteByIdUser(String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setCode(ErrorCodeDefs.ERR_SERVER_ERROR);
            errorResponse.setMessage((Constants.ErrorMessageUserValidation.USER_NOT_FOUND));
            return ResponseEntity.ok(errorResponse);
        } else {
            List<Classroom> classrooms = userOptional.get().getClassrooms();
            classroomRepository.deleteAll(classrooms);
            userRepository.deleteById(id);
            return ResponseEntity.ok(true);
        }

    }

    @Override
    @Transactional
    public List<UserDTO> deleteAllIdUser(List<String> ids) {
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : userRepository.findAllById(ids)) {
            userDTOS.add(modelMapper.map(user, UserDTO.class));
        }
        userRepository.deleteAllByIdInBatch(ids);
        return userDTOS;
    }


//    @Overri


    public static String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public static boolean isValidPassword(String password) {
        if (password.length() < 6) {
            throw new MyCustomException("Mật khẩu phải có ít nhất 6 ký tự!");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new MyCustomException("Mật khẩu phải chưa ít nhất một chữ hoa!");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new MyCustomException("Mật khẩu phải chưa ít nhất một chữ thường!");
        }
        if (!password.matches(".*\\d.*")) {
            throw new MyCustomException("Mật khẩu phải chứa ít nhất một chữ số.");
        }
        if (!password.matches(".*[@#$%^&+=].*")) {
            throw new MyCustomException("Mật khẩu phải chứa ít nhất một ký tự đặc biệt.");
        }
        return true;
    }


    private Role buildRole(String roleId) {
        return roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role không tồn tại!"));
    }

    private void checkRoleIsValid(String roleId) {
        if (roleId == null)
            return;
        Role role = buildRole(roleId);
        if (role == null) {
            throw new MyCustomException("Vai trò không tồn tại!");
        }
    }


    private void checkUserIsExistByName(String name, String id) {
        if (StringUtils.hasText(id) && userRepository.existsByUserCode(name))
            throw new MyCustomException("Tên đăng nhập đã tồn tại!");

    }

    private void validateUserExist(String id) {
        boolean isExist = userRepository.existsById(id);
        if (!isExist) {
            throw new MyCustomException("Người dùng không tồn tại trên hệ thống!");
        }
    }

    private List<Role> buildRole(List<String> roleIds) {
        return CollectionUtils.isEmpty(roleIds) ? new ArrayList<>() : roleRepository.findAllById(roleIds);
    }

    @Override
    public ResponseEntity<?> createUserByExcel(MultipartFile file) {
        Role role = roleRepository.findById("STUDENT").orElseThrow();
        ReadDataFromExcel<User> readDataFromExcel = new ReadDataFromExcel<>(User.class, new UserRowMapper());
        List<User> users = readDataFromExcel.readFromExcel(file);
        List<User> users1 = users.stream().map(user -> {
            if (!userRepository.existsByUserCode(user.getUserCode())) {
                user.setRole(role);
                user.setId(UUID.randomUUID().toString());
                user.setPassword(passwordEncoder.encode("1111"));
                return user;
            }

            return null;
        }).filter(user -> user != null).collect(Collectors.toList());

        userRepository.saveAll(users1);
        return ResponseEntity.ok(users1);
    }


    @Override
    public ResponseEntity<?> updateUser(UpdateUserRequest request) {
        Optional<User> user = userRepository.findById(request.getId());
        if (user.isEmpty()) {
            BaseResponseError baseResponseError = new BaseResponseError();
            baseResponseError.setFailed(500 ,Constants.ErrorMessageUserValidation.USER_NOT_FOUND );
            return ResponseEntity.ok(baseResponseError);
        }
        userUpdateMapper.updateUserFromDto(request, user.get());
        userRepository.save(user.get());
//        user.get().setPassword(passwordEncoder.encode(request.getPassword()));
        BaseItemResponse response = new BaseItemResponse();
        response.successData(modelMapper.map(user.get() , UserDTO.class));

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getImagesByUser(String userCode)  {
        try {
            String json = "{\"user_code\" : \"" + userCode + "\" }";

          Object responseObject = BaseServicePython.callData("http://localhost:8000/get_images/" ,json );
            return ResponseEntity.ok(responseObject);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok( e.getMessage());
        }


    }

    @Override
    public ResponseEntity<?> createImages(MultipartFile file) throws IOException {
        String apiURL = "http://localhost:8088/detections/";
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(apiURL);

        // Tạo MultipartEntity để chứa dữ liệu hình ảnh và truyền vào request
        File fileZip = FaceRecogClient.convertMultipartFileToFile(file);
        HttpEntity entity = MultipartEntityBuilder.create()
                .addBinaryBody("input_folder", fileZip, ContentType.APPLICATION_OCTET_STREAM, file.getName())
                .build();

        httpPost.setEntity(entity);

        // Gửi yêu cầu và nhận phản hồi
        HttpResponse response = httpClient.execute(httpPost);

        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        // Cleanup: Delete the temporary file
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(result.toString());
        // Lấy giá trị từ trường "predicts"
        String message = rootNode.get("message").asText();
        fileZip.delete();
        BaseItemResponse response1 = new BaseItemResponse();
        response1.setSuccess(true);
        response1.setData(message);


        return ResponseEntity.ok(response1);
    }

    @Override
    public ResponseEntity<?> createModelByUserCode(List<String> userCodes) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonUserCodes = objectMapper.writeValueAsString(userCodes);

// Tạo chuỗi JSON chứa danh sách userCodes
        String json = "{\"ids\": " + jsonUserCodes + "}";
        Object results = null;
        try {
            results = BaseServicePython.callData("http://localhost:8000/training_users/" , json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(results);
    }

    @Override
    public ResponseEntity<?> filterUser(FilterUserRequest request) {
        Specification<User> specification = CustomUserRepository.filterUser(request);
        List<UserDTO> users = userRepository.findAll(specification).stream().map(user ->mapper.map(user , UserDTO.class)).collect(Collectors.toList());
        BaseListResponse baseListResponse = new BaseListResponse();
        baseListResponse.setSuccess(true);
        baseListResponse.setResults(users , users.size());
        return ResponseEntity.ok(baseListResponse);
    }

    @Override
    public ResponseEntity<?> filterUsers(FilterUserRequest request, String classroomId) {
        Optional<Classroom> classroom = classroomRepository.findById(classroomId);
        Specification<User> specification = CustomUserRepository.filterUser(request);

        if(classroom.isEmpty()){
            return ResponseEntity.ok("Khong ton tai classroom ") ;
        }
        List<User> users = classroom.get().getClassUsers().stream().map(ClassUser::getUser).collect(Collectors.toList());
//        List<UserDTO> userDTOS = users(specification)
        return null;
    }

    @Override
    public ResponseEntity<?> updateImage(String fileName, MultipartFile file) throws IOException {
        Object results = null ;
        File file1 = FaceRecogClient.convertMultipartFileToFile(file);

        HttpEntity entity = MultipartEntityBuilder.create()
                .addBinaryBody("image" , file1 , ContentType.APPLICATION_OCTET_STREAM , file.getName())
                .addTextBody("file_name" , fileName).build();
        results = BaseServicePython.getAPI("http://localhost:8000/replace_image/" , entity);
        BaseItemResponse response = new BaseItemResponse();
        response.setSuccess(true);
        response.setData(results);
        return ResponseEntity.ok(response);
    }
}
