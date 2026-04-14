package aulouu.timetracker.service;

import aulouu.timetracker.dto.requst.LoginUserRequest;
import aulouu.timetracker.dto.requst.RegisterUserRequest;
import aulouu.timetracker.dto.response.AuthResponse;
import aulouu.timetracker.exception.UserAlreadyExistException;
import aulouu.timetracker.exception.UserNotFoundException;
import aulouu.timetracker.model.User;
import aulouu.timetracker.repository.UserRepository;
import aulouu.timetracker.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterUserRequest registerUserRequest) {
        if (userRepository.existsByUsername(registerUserRequest.getUsername()))
            throw new UserAlreadyExistException(
                    String.format("Username %s already exists", registerUserRequest.getUsername())
            );

        User user = User
                .builder()
                .username(registerUserRequest.getUsername())
                .password(passwordEncoder.encode(registerUserRequest.getPassword()))
                .build();

        user = userRepository.save(user);
        String token = jwtUtils.generateJwtToken(user.getUsername());

        return new AuthResponse(
                user.getUsername(),
                token
        );
    }

    public AuthResponse login(LoginUserRequest loginUserRequest) {
        User user = userRepository.findByUsername(loginUserRequest.getUsername())
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("Username %s not found", loginUserRequest.getUsername())
                ));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserRequest.getUsername(), loginUserRequest.getPassword())
        );

        String token = jwtUtils.generateJwtToken(user.getUsername());

        return new AuthResponse(
                user.getUsername(),
                token
        );
    }
}
