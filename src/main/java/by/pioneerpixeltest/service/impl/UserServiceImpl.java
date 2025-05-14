package by.pioneerpixeltest.service.impl;


import by.pioneerpixeltest.dao.dto.UserDto;
import by.pioneerpixeltest.dao.dto.UserSearchDto;
import by.pioneerpixeltest.mapper.UserMapper;
import by.pioneerpixeltest.repository.UserRepository;
import by.pioneerpixeltest.service.UserService;
import by.pioneerpixeltest.util.UserSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto updateUser(UserDto userDto) {
        return null;
    }

    @Override
    public void transferMoney(UUID fromUserId, UUID toUserId, BigDecimal amount) {

    }

//    @Override
//    @Transactional
//    @CacheEvict(value = "users", key = "#id")
//    public UserDto updateUser(Long id, UserDto userDto) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        validateUserData(userDto);
//
//        user.setName(userDto.getName());
//        user.setDateOfBirth(userDto.getDateOfBirth());
////        user.getEmailData().clear();
////        user.getEmailData().addAll(userDto.getEmails().stream()
////                .map(email -> {
////                    EmailData emailData = new EmailData();
////                    emailData.setEmail(email);
////                    return emailData;
////                })
////                .toList());
////
////        user.getPhoneData().clear();
////        user.getPhoneData().addAll(userDto.getPhones().stream()
////                .map(phone -> {
////                    PhoneData phoneData = new PhoneData();
////                    phoneData.setPhone(phone);
////                    return phoneData;
////                })
////                .toList());
//
//        User savedUser = userRepository.save(user);
//        return convertToDto(savedUser);
//    }
//
//    @Override
//    @Transactional
//    public void transferMoney(Long fromUserId, Long toUserId, BigDecimal amount) {
//        User fromUser = userRepository.findById(fromUserId)
//                .orElseThrow(() -> new RuntimeException("Source user not found"));
//        User toUser = userRepository.findById(toUserId)
//                .orElseThrow(() -> new RuntimeException("Target user not found"));
//

    /// /        if (fromUser.getAccount().getBalance().compareTo(amount) < 0) {
    /// /            throw new RuntimeException("Insufficient funds");
    /// /        }
    /// /
    /// /        fromUser.getAccount().setBalance(fromUser.getAccount().getBalance().subtract(amount));
    /// /        toUser.getAccount().setBalance(toUser.getAccount().getBalance().add(amount));
//
//        userRepository.save(fromUser);
//        userRepository.save(toUser);
//    }
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
}