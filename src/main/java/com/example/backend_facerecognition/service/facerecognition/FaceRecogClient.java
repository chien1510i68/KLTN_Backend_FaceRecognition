package com.example.backend_facerecognition.service.facerecognition;

import com.example.backend_facerecognition.model.*;
import com.example.backend_facerecognition.repository.ClassroomRepository;
import com.example.backend_facerecognition.repository.FaceRecognitionRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor

public class FaceRecogClient {
    private final ClassroomRepository classroomRepository;
    private final FaceRecognitionRepository faceRecognitionRepository ;
    public Integer predictFace(MultipartFile image_data ,String userCode) throws IOException {
        String apiURL = "http://localhost:8000/predict/";
//        String apiURL = "http://localhost:";
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(apiURL);

        // Tạo MultipartEntity để chứa dữ liệu hình ảnh và truyền vào request
        File file = convertMultipartFileToFile(image_data);
        HttpEntity entity = MultipartEntityBuilder.create()
                .addBinaryBody("image", file, ContentType.APPLICATION_OCTET_STREAM, file.getName())
                .addTextBody("file_name",userCode )
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
        Integer predictsValue = rootNode.get("predictions").asInt();
        file.delete();

        return predictsValue;
    }

    public static File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        try (var inputStream = file.getInputStream()) {
            Files.copy(inputStream, convertedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        return convertedFile;
    }

    public User checkUserInClassroom(QRCode qrcode, String userCode) {
        Optional<Classroom> classroom = classroomRepository.findById(qrcode.getClassroomId());
        if (classroom.isEmpty()) {
            return null;
        }
        Optional<ClassUser> user = classroom.get().getClassUsers().stream().filter(classUser -> classUser.getUser().getUserCode().equals(userCode)).findFirst();
        return (user.isPresent()) ? user.get().getUser() : null;
    }

    public boolean checkFaceSectionExistInRepo (QRCode qrCode , User user){
        List<FaceRecognitionSection> faceRecognitionSections = faceRecognitionRepository.findAllByUserAndQrCode(user, qrCode);
        return (faceRecognitionSections.size() > 0) ? true : false ;
    }

    public boolean checkTime(QRCode qrCode , Timestamp currentTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        LocalDateTime startTime = qrCode.getCreateAt().toLocalDateTime();
        LocalDateTime timeCurrent = currentTime.toLocalDateTime();
        LocalDateTime endTime = qrCode.getEndTime().toLocalDateTime();

        return (timeCurrent.isAfter(startTime) && timeCurrent.isBefore(endTime));

    }

}
