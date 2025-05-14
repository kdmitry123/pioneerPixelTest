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
import by.pioneerpixeltest.util.UserSpecification;
import by.pioneerpixeltest.util.ValidationUtil;
import jakarta.annotation.PreDestroy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
//@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final Map<UUID, BigDecimal> initialBalances = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        userRepository.findAll().forEach(user ->
                initialBalances.put(user.getId(), user.getAccount().getBalance())
        );
        scheduler.scheduleAtFixedRate(this::increaseBalances, 0, 10, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void shutdown() {
        scheduler.shutdown();
    }

    @Override
    @Transactional
    @CachePut(value = "users", key = "#userDto.id")
    public UserDto updateUser(UserDto userDto) {
        ValidationUtil.validateUserDataForUpdate(userDto);
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> {
                    log.info("User with id = {} not found", userDto.getId());
                    return new UserValidationException("User not found");
                });
        return UserMapper.convertToDto(updateUserFields(user, userDto));
    }

    @Override
    public void transferMoney(UUID fromUserId, UUID toUserId, BigDecimal amount) {

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

    @Transactional
    public void increaseBalances() {
        log.info("Balance increase task iteration");
        userRepository.findAll().forEach(user -> {
            BigDecimal currentBalance = user.getAccount().getBalance();
            BigDecimal initialBalance = initialBalances.get(user.getId());
            if (initialBalance == null) {
                initialBalance = currentBalance;
                initialBalances.put(user.getId(), initialBalance);
                log.info("Initial balance set for new user {}: {}", user.getId(), initialBalance);
            }
            if (initialBalance.compareTo(BigDecimal.ZERO) == 0) {
                initialBalance = new BigDecimal("0.1");
                initialBalances.put(user.getId(), initialBalance);
                log.info("Initial balance adjusted for user {}: 0 -> 0.1", user.getId());
            }
            BigDecimal maxBalance = initialBalance.multiply(new BigDecimal("2.07"));
            BigDecimal increasedBalance = currentBalance.multiply(new BigDecimal("1.10"));
            if (currentBalance.compareTo(BigDecimal.ZERO) == 0) {
                increasedBalance = new BigDecimal("0.1");
            }
            if (increasedBalance.compareTo(maxBalance) <= 0) {
                user.getAccount().setBalance(increasedBalance);
                saveAndCacheUser(user);
                log.info("Increased balance for user {}: {} -> {}",
                        user.getId(), currentBalance, increasedBalance);
            } else {
                log.info("Balance increase skipped for user {} - would exceed maximum",
                        user.getId());
            }
        });
    }

    @CachePut(value = "users", key = "#user.id")
    public UserDto saveAndCacheUser(User user) {
        User savedUser = userRepository.save(user);
        return UserMapper.convertToDto(savedUser);
    }

    private void registerNewUser(User user) {                // если создается новый пользователь, то после сохранения добавить вызов registerNewUser(savedUser);
        BigDecimal initialBalance = user.getAccount().getBalance();
        initialBalances.put(user.getId(), initialBalance);
        log.info("Registered new user {} with initial balance {}", user.getId(), initialBalance);
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