package com.example.backend_facerecognition.service.python;

import com.example.backend_facerecognition.dto.entity.ImagesUserDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Data

@Builder
public class BaseServicePython {
    public static Object callData(String url , String json) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url);

        httpPost.setEntity(new StringEntity(json, "UTF-8"));

        HttpResponse response = httpClient.execute(httpPost);

        String responseString = EntityUtils.toString(response.getEntity());
        ObjectMapper objectMapper = new ObjectMapper();

        Object responseObject = objectMapper.readValue(responseString, Object.class);

        return responseObject;
    }

    public static Object getAPI(String url , HttpEntity entity) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        HttpResponse response = httpClient.execute(httpPost);
        String responseString = EntityUtils.toString(response.getEntity());
        ObjectMapper objectMapper = new ObjectMapper();

        Object responseObject = objectMapper.readValue(responseString, Object.class);

        return responseObject;



    }

}
