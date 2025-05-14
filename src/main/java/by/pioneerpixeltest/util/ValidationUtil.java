package by.pioneerpixeltest.util;

import by.pioneerpixeltest.dao.dto.UserDto;
import by.pioneerpixeltest.exception.UserValidationException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationUtil {

    public static void validateUserData(UserDto userDto) {
        if (userDto.getName() == null || userDto.getName().trim().isEmpty()) {
            throw new UserValidationException("Name is required");
        }
        if (userDto.getDateOfBirth() == null) {
            throw new UserValidationException("Date of birth is required");
        }
        if (userDto.getEmails() == null || userDto.getEmails().isEmpty()) {
            throw new UserValidationException("At least one email is required");
        }
        if (userDto.getPhones() == null || userDto.getPhones().isEmpty()) {
            throw new UserValidationException("At least one phone is required");
        }
    }

}
