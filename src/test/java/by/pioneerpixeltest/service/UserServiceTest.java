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
