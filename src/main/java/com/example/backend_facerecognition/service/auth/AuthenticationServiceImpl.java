package com.example.backend_facerecognition.service.auth;

import com.example.backend_facerecognition.constant.Constants;
import com.example.backend_facerecognition.constant.ErrorCodeDefs;
import com.example.backend_facerecognition.dto.entity.UserDTO;
import com.example.backend_facerecognition.dto.request.user_request.CreateUserRequest;
import com.example.backend_facerecognition.dto.response.BaseItemResponse;
import com.example.backend_facerecognition.dto.response.ErrorResponse;
import com.example.backend_facerecognition.dto.response.LoginResponse;
import com.example.backend_facerecognition.exception.DataNotFoundException;
import com.example.backend_facerecognition.model.Role;
import com.example.backend_facerecognition.model.User;
import com.example.backend_facerecognition.model.UserDetailsImpl;
import com.example.backend_facerecognition.repository.RoleRepository;
import com.example.backend_facerecognition.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ModelMapper mapper;
    private final String cannotLogin = "Tài khoản hoặc mật khẩu không chính xác";

    @Value("${jwt.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Override
    public ResponseEntity<?> authenticateUser(String userCode, String password) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userCode, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateTokenWithAuthorities(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        BaseItemResponse response = new BaseItemResponse();
        response.setSuccess(true);
        response.setData(new LoginResponse(
                jwt,
                userDetails.getUsername(),
                userDetails.getUserCode(), roles

        ));

        return ResponseEntity.ok(response);

    }

    @Override
    public ResponseEntity<?> registerAccount(CreateUserRequest request) {
        if (userRepository.existsByUserCode(request.getUserCode())) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage(ErrorCodeDefs.getMessage(ErrorCodeDefs.USER_EXISTED));
            return ResponseEntity.ok(errorResponse);
        } else {
            Role role = roleRepository.findById("STUDENT").orElseThrow(() -> new DataNotFoundException(Constants.ErrorMessageRoleValidation.ROLE_NOT_FOUND));
            User user = User.builder()
                    .fullName(request.getFullName())
                    .dob(request.getDob())
                    .id(UUID.randomUUID().toString())
                    .classname(request.getClassname())
                    .userCode(request.getUserCode())
                    .address(null)
                    .phoneNumber(request.getPhoneNumber())
                    .role(role)
                    .password(passwordEncoder.encode(request.getPassword())).build();

            BaseItemResponse response = new BaseItemResponse();
            response.setSuccess(true);
            response.successData(mapper.map(user, UserDTO.class));
            userRepository.save(user);
            return ResponseEntity.ok(response);
        }


    }
}
