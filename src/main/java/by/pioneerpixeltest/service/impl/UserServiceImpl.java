package by.pioneerpixeltest.service.impl;


import by.pioneerpixeltest.dao.dto.UserDto;
import by.pioneerpixeltest.dao.dto.UserSearchDto;
import by.pioneerpixeltest.dao.entity.EmailData;
import by.pioneerpixeltest.dao.entity.PhoneData;
import by.pioneerpixeltest.dao.entity.User;
import by.pioneerpixeltest.exception.UserValidationException;
import by.pioneerpixeltest.mapper.UserMapper;
import by.pioneerpixeltest.repository.UserRepository;
import by.pioneerpixeltest.service.UserService;
import by.pioneerpixeltest.util.SecurityUtils;
import by.pioneerpixeltest.util.UserSpecification;
import by.pioneerpixeltest.util.ValidationUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    @CachePut(value = "users", key = "#userDto.id")
    public UserDto updateUser(UserDto userDto) {
        if (!SecurityUtils.getCurrentUserId().equals(userDto.getId())) {
            throw new AccessDeniedException("You can only update your own data");
        }
        ValidationUtil.validateUserDataForUpdate(userDto);
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> {
                    log.info("User with id = {} not found", userDto.getId());
                    return new UserValidationException("User not found");
                });
        return UserMapper.convertToDto(updateUserFields(user, userDto));
    }

    @Override
    public void transferMoney(UUID toUserId, BigDecimal amount) {

    }

    @Cacheable(value = "users", key = "#id")
    @Transactional
    public UserDto getUserById(UUID id) {
        return userRepository.findById(id)
                .map(UserMapper::convertToDto)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    @Transactional
    @Cacheable(value = "users", key = "#searchDto.toString()")
    public Page<UserDto> searchUsers(UserSearchDto searchDto, Pageable pageable) {
        return userRepository.findAll(UserSpecification.userFilter(searchDto), pageable)
                .map(UserMapper::convertToDto);
    }


    @CachePut(value = "users", key = "#user.id")
    public UserDto saveAndCacheUser(User user) {
        User savedUser = userRepository.save(user);
        return UserMapper.convertToDto(savedUser);
    }

    private User updateUserFields(User user, UserDto userDto) {
        if (userDto.getEmails() != null) {
            ValidationUtil.validateEmailExistence(userDto, userRepository);
            updateUserEmails(user, userDto.getEmails());
        }
        if (userDto.getPhones() != null) {
            ValidationUtil.validatePhoneExistence(userDto, userRepository);
            updateUserPhones(user, userDto.getPhones());
        }
        return userRepository.save(user);
    }

    private void updateUserEmails(User user, List<String> emails) {
        user.getEmailData().clear();
        user.getEmailData().addAll(emails.stream()
                .map(email -> {
                    EmailData emailData = new EmailData();
                    emailData.setEmail(email);
                    emailData.setUser(user);
                    return emailData;
                })
                .toList());
    }

    private void updateUserPhones(User user, List<String> phones) {
        user.getPhoneData().clear();
        user.getPhoneData().addAll(phones.stream()
                .map(phone -> {
                    PhoneData phoneData = new PhoneData();
                    phoneData.setPhone(phone);
                    phoneData.setUser(user);
                    return phoneData;
                })
                .toList());
    }

}