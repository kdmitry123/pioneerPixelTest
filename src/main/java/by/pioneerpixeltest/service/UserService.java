package by.pioneerpixeltest.service;

import by.pioneerpixeltest.dao.dto.UserDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface UserService {


    UserDto updateUser(UserDto userDto);

    void transferMoney(UUID fromUserId, UUID toUserId, BigDecimal amount);

    UserDto getUserById(UUID id);
}
