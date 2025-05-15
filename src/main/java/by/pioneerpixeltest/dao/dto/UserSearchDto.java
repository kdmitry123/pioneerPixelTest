package by.pioneerpixeltest.dao.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserSearchDto {
    private String phone;
    private String name;
    private String email;
    private LocalDate dateOfBirth;
}
