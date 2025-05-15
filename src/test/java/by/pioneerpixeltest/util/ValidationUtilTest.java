package by.pioneerpixeltest.util;

import by.pioneerpixeltest.dao.dto.UserDto;
import by.pioneerpixeltest.exception.UserValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidationUtilTest {

    @Test
    void validateUserDataForUpdate_WithValidData_ShouldNotThrowException() {
        UserDto userDto = new UserDto();
        userDto.setId(UUID.randomUUID());
        userDto.setEmails(List.of("test@example.com"));
        userDto.setPhones(List.of("123456789"));

        assertDoesNotThrow(() -> ValidationUtil.validateUserDataForUpdate(userDto));
    }

    @Test
    void validateUserDataForUpdate_WithNullEmailsAndPhones_ShouldNotThrowException() {
        UserDto userDto = new UserDto();
        userDto.setId(UUID.randomUUID());
        userDto.setEmails(null);
        userDto.setPhones(null);

        assertDoesNotThrow(() -> ValidationUtil.validateUserDataForUpdate(userDto));
    }

    @Test
    void validateUserDataForUpdate_WithEmptyEmailsAndPhones_ShouldNotThrowException() {
        UserDto userDto = new UserDto();
        userDto.setId(UUID.randomUUID());
        userDto.setEmails(Collections.emptyList());
        userDto.setPhones(Collections.emptyList());

        assertDoesNotThrow(() -> ValidationUtil.validateUserDataForUpdate(userDto));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "@example.com",
            "test@",
            "test.example.com",
            "test@@example.com",
            "test@ex ample.com"
    })
    void validateUserDataForUpdate_WithInvalidEmailFormat_ShouldThrowException(String email) {
        UserDto userDto = new UserDto();
        userDto.setId(UUID.randomUUID());
        userDto.setEmails(List.of(email));
        assertThrows(UserValidationException.class,
                () -> ValidationUtil.validateUserDataForUpdate(userDto));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "dsf1234567890123",
            "12345678901234"
    })
    void validateUserDataForUpdate_WithTooLongPhone_ShouldThrowException(String phone) {
        UserDto userDto = new UserDto();
        userDto.setId(UUID.randomUUID());
        userDto.setPhones(List.of(phone));

        assertThrows(UserValidationException.class,
                () -> ValidationUtil.validateUserDataForUpdate(userDto));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "0123456789",
            "12a3456789",
            "12.3456789",
            "12-3456789",
            "+123456789"
    })
    void validateUserDataForUpdate_WithInvalidPhoneFormat_ShouldThrowException(String phone) {
        UserDto userDto = new UserDto();
        userDto.setId(UUID.randomUUID());
        userDto.setPhones(List.of(phone));

        assertThrows(UserValidationException.class,
                () -> ValidationUtil.validateUserDataForUpdate(userDto));
    }
}
