package by.pioneerpixeltest.mapper;

import by.pioneerpixeltest.dao.dto.UserDto;
import by.pioneerpixeltest.dao.entity.EmailData;
import by.pioneerpixeltest.dao.entity.PhoneData;
import by.pioneerpixeltest.dao.entity.User;

import java.util.stream.Collectors;


public class UserMapper {

    public static UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setDateOfBirth(user.getDateOfBirth() != null ? user.getDateOfBirth().toLocalDate() : null);
        dto.setEmails(user.getEmailData().stream()
                .map(EmailData::getEmail)
                .collect(Collectors.toList()));
        dto.setPhones(user.getPhoneData().stream()
                .map(PhoneData::getPhone)
                .collect(Collectors.toList()));
        dto.setBalance(user.getAccount().getBalance());
        return dto;
    }

}
