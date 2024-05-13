package com.example.backend_facerecognition.service.mapper;

import com.example.backend_facerecognition.common.RowMapper;
import com.example.backend_facerecognition.model.User;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.UUID;

public class UserRowMapper implements RowMapper {

    @Override
    public User mapRow(Class clazz, Row row) {
        User user = new User();
        Cell cell1 = row.getCell(1);
        if (cell1 != null) {
            user.setUserCode(cell1.getStringCellValue());
        }

        Cell cell2 = row.getCell(2);
        Cell cell3 = row.getCell(3);
        if (cell2 != null && cell3 != null) {
            String fullName = cell2.getStringCellValue().concat(" " + cell3.getStringCellValue());
            user.setFullName(fullName);
        }

        Cell cell4 = row.getCell(4);
        if (cell4 != null) {
            user.setDob(cell4.getStringCellValue());
        }

        Cell cell5 = row.getCell(5);
        if (cell5 != null) {
            user.setClassname(cell5.getStringCellValue());
        }


        return user;
    }

}
