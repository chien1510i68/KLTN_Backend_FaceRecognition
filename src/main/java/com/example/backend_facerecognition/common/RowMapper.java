package com.example.backend_facerecognition.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

@Component
public interface RowMapper<T> {
    T mapRow(Class<T> clazz , Row row);
}
