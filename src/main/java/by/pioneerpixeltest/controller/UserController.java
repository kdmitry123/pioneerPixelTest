package by.pioneerpixeltest.controller;


import by.pioneerpixeltest.dao.dto.UserDto;
import by.pioneerpixeltest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

//    @PostMapping
//    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
//        return ResponseEntity.ok(userService.createUser(userDto));
//    }
//
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
    public ResponseEntity<UserDto> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
