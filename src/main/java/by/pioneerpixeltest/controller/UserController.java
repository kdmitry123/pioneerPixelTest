package by.pioneerpixeltest.controller;

import by.pioneerpixeltest.dao.dto.UserDto;
import by.pioneerpixeltest.dao.dto.UserSearchDto;
import by.pioneerpixeltest.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto) {
        log.info("UserController: updateUser: receive userDto: {}", userDto);
        return ResponseEntity.ok(userService.updateUser(userDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
        log.info("UserController: getUserById: receive id: {}", id);
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public Page<UserDto> searchUsers(UserSearchDto searchDto, Pageable pageable) {
        log.info("UserController: searchUsers: receive searchDto: {}", searchDto);
        return userService.searchUsers(searchDto, pageable);
    }
}
