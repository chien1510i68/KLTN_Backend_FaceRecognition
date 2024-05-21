package com.example.backend_facerecognition.service.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service

public class HandleImageIplm  implements HandleImageService {
    @Override
    public ResponseEntity<?> saveFile(byte[] imageData, String classroomId, String userCode) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM");
        String date = dateFormat.format(new Date());
        String basePath = "src/main/resources/signature/";

        // Tạo đường dẫn đầy đủ đến file
        String folderPath = basePath + classroomId + "/" + userCode + "/";
        String filePath = folderPath + date;

        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();  // Tạo tất cả các thư mục cha nếu chưa tồn tại
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
    public ResponseEntity<?> getSignature(String classroomId, String date, String userCode) {
        String basePath = "src/main/resources/signature/";
        String folderPath = basePath + classroomId + "/" + userCode + "/";
        List<FileData> fileList = new ArrayList<>();

        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        try {
                            byte[] fileContent = Files.readAllBytes(Paths.get(file.getPath()));
                            fileList.add(new FileData(file.getName(), fileContent));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }


        }
        return ResponseEntity.ok().body(fileList);


    }

    @Data
    @AllArgsConstructor
    static class FileData {
        private String fileName;
        private byte[] fileContent;
    }
}
