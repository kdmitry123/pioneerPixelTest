package by.pioneerpixeltest.mapper;

import by.pioneerpixeltest.dao.dto.UserDto;
import by.pioneerpixeltest.dao.entity.EmailData;
import by.pioneerpixeltest.dao.entity.PhoneData;
import by.pioneerpixeltest.dao.entity.User;

import java.math.BigDecimal;

public class UserMapper {

    private UserMapper() {
    }

    public static UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setEmails(user.getEmailData().stream()
                .map(EmailData::getEmail)
                .toList());
        dto.setPhones(user.getPhoneData().stream()
                .map(PhoneData::getPhone)
                .toList());
        dto.setBalance(user.getAccount() != null ? user.getAccount().getBalance() : BigDecimal.ZERO);
        return dto;
    }
}
