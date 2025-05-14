package by.pioneerpixeltest.dao.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private String name;
    private LocalDate dateOfBirth;
    private List<String> emails;
    private List<String> phones;
    private BigDecimal balance;
}
