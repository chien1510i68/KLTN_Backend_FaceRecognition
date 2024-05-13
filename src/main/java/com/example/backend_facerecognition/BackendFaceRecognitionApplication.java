package com.example.backend_facerecognition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BackendFaceRecognitionApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendFaceRecognitionApplication.class, args);
    }

}
