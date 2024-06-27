package com.example.backend_facerecognition.service.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service

public class HandleImageIplm  implements HandleImageService {
    @Override
    public ResponseEntity<?> saveFile(byte[] imageData, String classroomId, String userCode , Timestamp timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_HH");
        String formattedTime = dateFormat.format(timestamp);
        String basePath = "src/main/resources/signature/";

        String folderPath = basePath + classroomId + "/" + userCode + "/";
        String filePath = folderPath + formattedTime;

        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File imageFile = new File(filePath);
        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            fos.write(imageData);
            return ResponseEntity.ok().body("Lưu file thành công");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getSignature(String classroomId, Timestamp timestamp, String userCode) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_HH");
        String formattedTime = dateFormat.format(timestamp);
        String basePath = "src/main/resources/signature/";

        // Tạo đường dẫn đầy đủ đến file
        String folderPath = basePath + classroomId + "/" + userCode + "/";
        String filePath = folderPath + formattedTime;
        File imageFile = new File(filePath);
        if (imageFile.exists()) {
            try {
                byte[] imageBytes = Files.readAllBytes(Paths.get(filePath));
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);

                return base64Image;
            } catch (IOException e) {
              return null ;
            }
        }
        return null ;

    }

    @Data
    @AllArgsConstructor
    static class FileData {
        private String fileName;
        private byte[] fileContent;
    }
}
