package by.pioneerpixeltest.service.impl;


import by.pioneerpixeltest.dao.dto.UserDto;
import by.pioneerpixeltest.dao.entity.EmailData;
import by.pioneerpixeltest.dao.entity.PhoneData;
import by.pioneerpixeltest.dao.entity.User;
import by.pioneerpixeltest.repository.UserRepository;
import by.pioneerpixeltest.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        validateUserData(userDto);

        User user = new User();
        user.setName(userDto.getName());
        user.setDateOfBirth(userDto.getDateOfBirth());

//        Account account = new Account();
//        account.setBalance(userDto.getBalance());

//        user.setAccount(account);

//        usersetEmailData(userDto.getEmails().stream()
//                .map(email -> {
//                    EmailData emailData = new EmailData();
//                    emailData.setEmail(email);
//                    return emailData;
//                })
//                .collect(Collectors.toList()));
//
//        user.setPhoneData(userDto.getPhones().stream()
//                .map(phone -> {
//                    PhoneData phoneData = new PhoneData();
//                    phoneData.setPhone(phone);
//                    return phoneData;
//                })
//                .collect(Collectors.toList()));

        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        return null;
    }

    @Override
    public void transferMoney(Long fromUserId, Long toUserId, BigDecimal amount) {

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
//    @Cacheable(value = "users", key = "#id")
    @Transactional
    public UserDto getUserById(UUID id) {
        return userRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private void validateUserData(UserDto userDto) {
        if (userDto.getName() == null || userDto.getName().trim().isEmpty()) {
            throw new RuntimeException("Name is required");
        }
        if (userDto.getDateOfBirth() == null) {
            throw new RuntimeException("Date of birth is required");
        }
        if (userDto.getEmails() == null || userDto.getEmails().isEmpty()) {
            throw new RuntimeException("At least one email is required");
        }
        if (userDto.getPhones() == null || userDto.getPhones().isEmpty()) {
            throw new RuntimeException("At least one phone is required");
        }
    }

    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setDateOfBirth(user.getDateOfBirth());
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