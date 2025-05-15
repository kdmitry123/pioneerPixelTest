package by.pioneerpixeltest.dao.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@ToString
public class UserDto implements Serializable {
    @NotNull
    private UUID id;
    private String name;
    private LocalDate dateOfBirth;
    private List<String> emails;
    private List<String> phones;
    private BigDecimal balance;
}
