package by.pioneerpixeltest.service;

import by.pioneerpixeltest.dao.dto.UserDto;
import by.pioneerpixeltest.dao.entity.Account;
import by.pioneerpixeltest.dao.entity.User;
import by.pioneerpixeltest.exception.UserValidationException;
import by.pioneerpixeltest.repository.UserRepository;
import by.pioneerpixeltest.service.impl.UserServiceImpl;
import by.pioneerpixeltest.util.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    private User testUser;
    private UUID testUserId;

    @BeforeEach
    void init() {
        testUserId = UUID.randomUUID();
        testUser = new User();
        testUser.setId(testUserId);
        testUser.setName("Test User");
        testUser.setDateOfBirth(LocalDate.of(1990, 1, 1));
        testUser.setEmailData(new ArrayList<>());
        testUser.setPhoneData(new ArrayList<>());

        Account account = new Account();
        account.setBalance(BigDecimal.TEN);
        account.setUser(testUser);
        testUser.setAccount(account);
    }

    @Test
    void getUserById_WhenUserExists_ReturnsUserDto() {
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));

        UserDto result = userService.getUserById(testUserId);

        assertNotNull(result);
        assertEquals(testUserId, result.getId());
        assertEquals("Test User", result.getName());
        assertEquals(LocalDate.of(1990, 1, 1), result.getDateOfBirth());
        assertEquals(BigDecimal.TEN, result.getBalance());
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ThrowsException() {
        UUID nonExistentUserId = UUID.randomUUID();

        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        assertThrows(UserValidationException.class, () -> userService.getUserById(nonExistentUserId));
    }

    @Test
    void updateUser_WhenUserExistsAndDataValid_ReturnsUpdatedUserDto() {
        UUID currentUserId = UUID.randomUUID();
        UserDto updateDto = new UserDto();
        updateDto.setId(currentUserId);
        updateDto.setEmails(List.of("test@example.com"));
        updateDto.setPhones(List.of("123456789"));
        User existingUser = new User();
        existingUser.setId(currentUserId);
        existingUser.setEmailData(new ArrayList<>());
        existingUser.setPhoneData(new ArrayList<>());
        try (MockedStatic<SecurityUtils> securityUtils = Mockito.mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(userRepository.findById(currentUserId)).thenReturn(Optional.of(existingUser));
            when(userRepository.save(any(User.class))).thenReturn(existingUser);

            UserDto result = userService.updateUser(updateDto);

            assertNotNull(result);
            assertEquals(currentUserId, result.getId());
            verify(userRepository).save(any(User.class));
        }
    }

    @Test
    void updateUser_WhenUpdatingOtherUserData_ThrowsAccessDeniedException() {
        UUID currentUserId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();
        UserDto updateDto = new UserDto();
        updateDto.setId(otherUserId);
        try (MockedStatic<SecurityUtils> securityUtils = Mockito.mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);

            assertThrows(AccessDeniedException.class, () -> userService.updateUser(updateDto));
            verify(userRepository, never()).findById(any());
        }
    }

    @Test
    void updateUser_WhenUserNotFound_ThrowsUserValidationException() {
        UUID userId = UUID.randomUUID();
        UserDto updateDto = new UserDto();
        updateDto.setId(userId);
        try (MockedStatic<SecurityUtils> securityUtils = Mockito.mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(userId);
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            assertThrows(UserValidationException.class, () -> userService.updateUser(updateDto));
            verify(userRepository).findById(userId);
            verify(userRepository, never()).save(any());
        }
    }

    @Test
    void updateUser_WhenInvalidData_ThrowsUserValidationException() {
        UUID userId = UUID.randomUUID();
        UserDto updateDto = new UserDto();
        updateDto.setId(userId);
        updateDto.setEmails(List.of("invalid-email"));
        try (MockedStatic<SecurityUtils> securityUtils = Mockito.mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(userId);

            assertThrows(UserValidationException.class, () -> userService.updateUser(updateDto));
            verify(userRepository, never()).save(any());
        }
    }
}
