package aulouu.timetracker.controller;

import aulouu.timetracker.dto.requst.LoginUserRequest;
import aulouu.timetracker.dto.requst.RegisterUserRequest;
import aulouu.timetracker.dto.response.AuthResponse;
import aulouu.timetracker.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @SecurityRequirements
    public AuthResponse register(@RequestBody @Valid RegisterUserRequest registerUserRequest) {
        return authService.register(registerUserRequest);
    }

    @PostMapping("/login")
    @SecurityRequirements
    public AuthResponse login(@RequestBody @Valid LoginUserRequest loginUserRequest) {
        return authService.login(loginUserRequest);
    }
}
