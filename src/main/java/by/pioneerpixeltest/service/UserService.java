package by.pioneerpixeltest.service;

import by.pioneerpixeltest.dao.dto.UserDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface UserService {

    UserDto createUser(UserDto userDto);

    UserDto updateUser(Long id, UserDto userDto);

    void transferMoney(Long fromUserId, Long toUserId, BigDecimal amount);

    UserDto getUserById(UUID id);
}
