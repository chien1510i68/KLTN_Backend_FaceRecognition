package com.example.backend_facerecognition.common;

import com.example.backend_facerecognition.constant.Constants;
import com.example.backend_facerecognition.dto.response.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Data
@RequiredArgsConstructor
public class ReadDataFromExcel<T> {
    private final Class<T> clazz;
    private final RowMapper<T> rowMapper;

    public List<T> readFromExcel(MultipartFile file) {
        List<T> data = new ArrayList<>();
        try {
            InputStream inputStream = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            // Bỏ qua dòng đầu tiên
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                T object = readRowData(row);
                data.add(object);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return data;
    }


    private T readRowData(Row row) {
        return rowMapper.mapRow(clazz, row);
    }

}




