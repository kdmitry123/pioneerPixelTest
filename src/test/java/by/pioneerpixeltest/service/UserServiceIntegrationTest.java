package by.pioneerpixeltest.service;

import by.pioneerpixeltest.PostgresTestContainer;
import by.pioneerpixeltest.dao.dto.UserDto;
import by.pioneerpixeltest.dao.dto.UserSearchDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceIntegrationTest extends PostgresTestContainer {
    @Autowired
    private UserService userService;

    @Test
    void getUserById_ShouldReturnUser() {
        UUID userId = UUID.fromString("fea81f3e-c260-46e3-8ecb-acdc3664b1c8");

        UserDto userDto = userService.getUserById(userId);

        assertNotNull(userDto);
        assertEquals(userId, userDto.getId());
        assertEquals("test1", userDto.getName());
        assertEquals(3, userDto.getEmails().size());
        assertEquals(3, userDto.getPhones().size());
        assertEquals(new BigDecimal("10.00"), userDto.getBalance());
        assertEquals(LocalDate.of(1990, 5, 13), userDto.getDateOfBirth());
    }

    @Test
    void searchUsers_WithEmptyFilter_ShouldReturnAllUsers() {
        UserSearchDto searchDto = new UserSearchDto();

        Page<UserDto> result = userService.searchUsers(searchDto, Pageable.unpaged());

        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
        assertEquals(27, result.getTotalElements());
    }

    @Test
    void searchUsers_WithNameFilter_ShouldReturnUser() {
        UserSearchDto searchDto = new UserSearchDto();
        searchDto.setName("test11");

        Page<UserDto> result = userService.searchUsers(searchDto, Pageable.unpaged());

        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals("test11", result.getContent().getFirst().getName());
    }

    @Test
    void searchUsers_WithNameFilter_ShouldReturnMatchingUsers() {
        UserSearchDto searchDto = new UserSearchDto();
        searchDto.setName("test1");

        Page<UserDto> result = userService.searchUsers(searchDto, Pageable.unpaged());

        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
        assertEquals(11, result.getTotalElements());
    }

    @Test
    void searchUsers_WithNotExistNameFilter_ShouldReturnEmptyPage() {
        UserSearchDto searchDto = new UserSearchDto();
        searchDto.setName("test1111");

        Page<UserDto> result = userService.searchUsers(searchDto, Pageable.unpaged());

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void searchUsers_WithPhoneFilter_ShouldReturnUser() {
        UserSearchDto searchDto = new UserSearchDto();
        searchDto.setPhone("312456789");

        Page<UserDto> result = userService.searchUsers(searchDto, Pageable.unpaged());

        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals(UUID.fromString("fea81f3e-c260-46e3-8ecb-acdc3664b1c6"), result.getContent().getFirst().getId());
    }

    @Test
    void searchUsers_WithNotExistPhoneFilter_ShouldReturnEmptyPage() {
        UserSearchDto searchDto = new UserSearchDto();
        searchDto.setPhone("34123413423423");

        Page<UserDto> result = userService.searchUsers(searchDto, Pageable.unpaged());

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void searchUsers_WithEmailFilter_ShouldReturnUser() {
        UserSearchDto searchDto = new UserSearchDto();
        searchDto.setEmail("juliet66@example.com");

        Page<UserDto> result = userService.searchUsers(searchDto, Pageable.unpaged());

        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals(UUID.fromString("9d665638-1d7e-45fd-97d5-7acc1d8ef18f"), result.getContent().getFirst().getId());
    }

    @Test
    void searchUsers_WithNotExistEmailFilter_ShouldReturnEmptyPage() {
        UserSearchDto searchDto = new UserSearchDto();
        searchDto.setEmail("defferent@example.com");

        Page<UserDto> result = userService.searchUsers(searchDto, Pageable.unpaged());

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void searchUsers_WithDateOfBirthFilter_ShouldReturnUser() {
        UserSearchDto searchDto = new UserSearchDto();
        searchDto.setDateOfBirth(LocalDate.of(1990, 5, 13));

        Page<UserDto> result = userService.searchUsers(searchDto, Pageable.unpaged());

        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals(UUID.fromString("48b7e652-c4d5-4ecf-9cfe-4c6903e10f58"), result.getContent().getFirst().getId());
    }

    @Test
    void searchUsers_WithMoreDateOfBirthFilter_ShouldReturnEmptyPage() {
        UserSearchDto searchDto = new UserSearchDto();
        searchDto.setDateOfBirth(LocalDate.of(1990, 5, 14));

        Page<UserDto> result = userService.searchUsers(searchDto, Pageable.unpaged());

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
    }

}
