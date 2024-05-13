package com.example.backend_facerecognition.security;

import com.example.backend_facerecognition.constant.ErrorCodeDefs;
import com.example.backend_facerecognition.dto.response.BaseResponseError;
import com.example.backend_facerecognition.dto.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.error("Unauthorized error: {}", authException.getMessage());
        BaseResponseError errorResponse = new BaseResponseError();
        errorResponse.setFailed(ErrorCodeDefs.TOKEN_INVALID , ErrorCodeDefs.getMessage(ErrorCodeDefs.TOKEN_INVALID));
        OutputStream responseStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(responseStream, errorResponse);
        responseStream.flush();
    }
}
