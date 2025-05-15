package by.pioneerpixeltest.util;

import by.pioneerpixeltest.dao.dto.UserDto;
import by.pioneerpixeltest.exception.UserValidationException;
import by.pioneerpixeltest.repository.UserRepository;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationUtil {

    public static void validateUserDataForUpdate(UserDto userDto) {
        validateUserEmails(userDto);
        validateUserPhones(userDto);
    }

    private void validateUserEmails(UserDto userDto) {
        if (userDto.getEmails() != null) {
            for (String email : userDto.getEmails()) {
                if (email.length() > 200) {
                    throw new UserValidationException("Email length must not exceed 200 characters");
                }
                if (!email.matches("^[A-Za-z0-9][A-Za-z0-9+_.-]*@[A-Za-z0-9.-]+$")) {
                    throw new UserValidationException("Email " + email + " has invalid format");
                }
            }
        }
    }

    private void validateUserPhones(UserDto userDto) {
        if (userDto.getPhones() != null) {
            for (String phone : userDto.getPhones()) {
                if (phone.length() > 13) {
                    throw new UserValidationException("Phone number length must not exceed 13 characters");
                }
                if (!phone.matches("^[1-9][0-9]*$")) {
                    throw new UserValidationException("Phone " + phone + " has invalid format");
                }
            }
        }
    }

    public static void validateEmailExistence(UserDto userDto, UserRepository repository) {
        for (String email : userDto.getEmails()) {
            if (repository.existsByEmailDataEmailAndIdNot(email, userDto.getId())) {
                throw new UserValidationException("Email " + email + " already exists");
            }
        }
    }

    public static void validatePhoneExistence(UserDto userDto, UserRepository repository) {
        for (String phone : userDto.getPhones()) {
            if (repository.existsByPhoneDataPhoneAndIdNot(phone, userDto.getId())) {
                throw new UserValidationException("Phone " + phone + " already exist");
            }
        }
    }
}
