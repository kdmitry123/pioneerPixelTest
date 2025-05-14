package by.pioneerpixeltest.controller;


import by.pioneerpixeltest.dao.dto.UserDto;
import by.pioneerpixeltest.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

//    @PutMapping("/{id}")
//    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
//        return ResponseEntity.ok(userService.updateUser(id, userDto));
//    }
//
//    @PostMapping("/{fromUserId}/transfer/{toUserId}")
//    public ResponseEntity<Void> transferMoney(
//            @PathVariable Long fromUserId,
//            @PathVariable Long toUserId,
//            @RequestParam BigDecimal amount) {
//        userService.transferMoney(fromUserId, toUserId, amount);
//        return ResponseEntity.ok().build();
//    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
        log.info("UserController: getUserById: receive id: {}", id);
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
