package by.pioneerpixeltest.controller;

import by.pioneerpixeltest.dao.entity.User;
import by.pioneerpixeltest.repository.UserRepository;
import by.pioneerpixeltest.security.AuthRequest;
import by.pioneerpixeltest.security.AuthResponse;
import by.pioneerpixeltest.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        User user = userRepository.findByName(request.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String token = jwtService.generateToken(user.getId());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
