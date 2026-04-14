package aulouu.timetracker.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import aulouu.timetracker.dto.response.AuthResponse;
import aulouu.timetracker.security.jwt.JwtAuthEntryPoint;
import aulouu.timetracker.security.jwt.JwtAuthTokenFilter;
import aulouu.timetracker.security.service.AuthUserDetailsService;
import aulouu.timetracker.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;
    @MockBean
    private JwtAuthTokenFilter jwtAuthTokenFilter;
    @MockBean
    private JwtAuthEntryPoint jwtAuthEntryPoint;
    @MockBean
    private AuthUserDetailsService authUserDetailsService;

    @Test
    void registerShouldReturnAuthResponse() throws Exception {
        AuthResponse response = new AuthResponse();
        response.setUsername("user123");
        response.setToken("jwt-token");
        when(authService.register(any())).thenReturn(response);

        String body = """
                {"username":"user123","password":"password1"}
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user123"))
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }

    @Test
    void loginShouldReturnBadRequestWhenValidationFails() throws Exception {
        String invalidBody = """
                {"username":"","password":"123"}
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }
}
