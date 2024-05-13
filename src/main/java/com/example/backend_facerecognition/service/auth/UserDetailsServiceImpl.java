package com.example.backend_facerecognition.service.auth;

import com.example.backend_facerecognition.model.User;
import com.example.backend_facerecognition.model.UserDetailsImpl;
import com.example.backend_facerecognition.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserCode(username).orElseThrow(() -> new RuntimeException("Không tìm mã sinh viên !"));
        return UserDetailsImpl.builder()
                .id(user.getId())
                .userCode(user.getUserCode())
                .userName(user.getFullName())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .phone(user.getPhoneNumber()).build();
    }
}
