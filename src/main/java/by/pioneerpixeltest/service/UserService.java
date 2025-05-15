package by.pioneerpixeltest.service;

import by.pioneerpixeltest.dao.dto.UserDto;
import by.pioneerpixeltest.dao.dto.UserSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
    UserDto updateUser(UserDto userDto);

    UserDto getUserById(UUID id);

    Page<UserDto> searchUsers(UserSearchDto searchDto, Pageable pageable);
}
