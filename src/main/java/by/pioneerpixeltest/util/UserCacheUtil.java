package by.pioneerpixeltest.util;

import by.pioneerpixeltest.dao.dto.UserDto;
import by.pioneerpixeltest.dao.entity.User;
import by.pioneerpixeltest.mapper.UserMapper;
import by.pioneerpixeltest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCacheUtil {
    private final UserRepository userRepository;
    private final CacheManager cacheManager;

    public UserDto saveUser(User user) {
        UserDto dto = UserMapper.convertToDto(userRepository.save(user));
        updateUserInCache(dto);
        return dto;
    }

    private void updateUserInCache(UserDto dto) {
        if (cacheManager != null && cacheManager.getCache("users") != null) {
            cacheManager.getCache("users").put(dto.getId(), dto);
        }
    }
}
