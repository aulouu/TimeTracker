package aulouu.timetracker.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import aulouu.timetracker.dto.response.EmployeeResponse;
import aulouu.timetracker.security.jwt.JwtAuthEntryPoint;
import aulouu.timetracker.security.jwt.JwtAuthTokenFilter;
import aulouu.timetracker.security.service.AuthUserDetailsService;
import aulouu.timetracker.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;
    @MockBean
    private JwtAuthTokenFilter jwtAuthTokenFilter;
    @MockBean
    private JwtAuthEntryPoint jwtAuthEntryPoint;
    @MockBean
    private AuthUserDetailsService authUserDetailsService;

    @Test
    void getEmployeeByIdShouldReturnEmployee() throws Exception {
        EmployeeResponse response = EmployeeResponse.builder()
                .id(1L)
                .firstName("Ivan")
                .lastName("Petrov")
                .build();
        when(employeeService.getEmployeeById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Ivan"));
    }

    @Test
    void createEmployeeShouldReturnBadRequestWhenInvalid() throws Exception {
        String invalidBody = """
                {"firstName":"","lastName":"Petrov"}
                """;

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }
}
