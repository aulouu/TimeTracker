package aulouu.timetracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aulouu.timetracker.dto.requst.LoginUserRequest;
import aulouu.timetracker.dto.requst.RegisterUserRequest;
import aulouu.timetracker.dto.response.AuthResponse;
import aulouu.timetracker.exception.UserAlreadyExistException;
import aulouu.timetracker.exception.UserNotFoundException;
import aulouu.timetracker.model.User;
import aulouu.timetracker.repository.UserRepository;
import aulouu.timetracker.security.jwt.JwtUtils;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerShouldThrowWhenUsernameExists() {
        RegisterUserRequest request = RegisterUserRequest.builder().username("existingUser").password("password").build();
        when(userRepository.existsByUsername("existingUser")).thenReturn(true);

        assertThrows(UserAlreadyExistException.class, () -> authService.register(request));
    }

    @Test
    void registerShouldSaveAndReturnToken() {
        RegisterUserRequest request = RegisterUserRequest.builder().username("newUser").password("password").build();
        User saved = User.builder().id(10L).username("newUser").password("encoded").build();

        when(userRepository.existsByUsername("newUser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(saved);
        when(jwtUtils.generateJwtToken("newUser")).thenReturn("jwt-token");

        AuthResponse response = authService.register(request);

        assertEquals("newUser", response.getUsername());
        assertEquals("jwt-token", response.getToken());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void loginShouldThrowWhenUserNotFound() {
        LoginUserRequest request = LoginUserRequest.builder().username("missing").password("password").build();
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> authService.login(request));
    }

    @Test
    void loginShouldAuthenticateAndReturnToken() {
        LoginUserRequest request = LoginUserRequest.builder().username("user1").password("password").build();
        User user = User.builder().id(1L).username("user1").password("encoded").build();

        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
        when(jwtUtils.generateJwtToken("user1")).thenReturn("jwt-token");

        AuthResponse response = authService.login(request);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        assertNotNull(response);
        assertEquals("user1", response.getUsername());
        assertEquals("jwt-token", response.getToken());
    }
}
