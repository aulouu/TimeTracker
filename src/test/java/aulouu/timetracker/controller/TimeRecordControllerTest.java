package aulouu.timetracker.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import aulouu.timetracker.dto.response.EmployeeResponse;
import aulouu.timetracker.dto.response.TaskResponse;
import aulouu.timetracker.dto.response.TimeRecordResponse;
import aulouu.timetracker.model.TaskStatus;
import aulouu.timetracker.security.jwt.JwtAuthEntryPoint;
import aulouu.timetracker.security.jwt.JwtAuthTokenFilter;
import aulouu.timetracker.security.service.AuthUserDetailsService;
import aulouu.timetracker.service.TimeRecordService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TimeRecordController.class)
@AutoConfigureMockMvc(addFilters = false)
class TimeRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TimeRecordService timeRecordService;
    @MockBean
    private JwtAuthTokenFilter jwtAuthTokenFilter;
    @MockBean
    private JwtAuthEntryPoint jwtAuthEntryPoint;
    @MockBean
    private AuthUserDetailsService authUserDetailsService;

    @Test
    void createTimeRecordShouldReturnResponse() throws Exception {
        TimeRecordResponse response = TimeRecordResponse.builder()
                .id(10L)
                .employee(EmployeeResponse.builder().id(1L).firstName("Ann").lastName("Doe").build())
                .task(TaskResponse.builder().id(2L).title("Task").description("Desc").status(TaskStatus.NEW).build())
                .startTime(LocalDateTime.of(2026, 1, 1, 9, 0))
                .endTime(LocalDateTime.of(2026, 1, 1, 10, 0))
                .workDescription("Work")
                .build();
        when(timeRecordService.createTimeRecord(any())).thenReturn(response);

        String body = """
                {
                  "employeeId": 1,
                  "taskId": 2,
                  "startTime": "2026-01-01T09:00:00",
                  "endTime": "2099-01-01T10:00:00",
                  "workDescription": "Work"
                }
                """;

        mockMvc.perform(post("/api/time-records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.workDescription").value("Work"));
    }

    @Test
    void getTimeRecordsByPeriodShouldReturnList() throws Exception {
        TimeRecordResponse response = TimeRecordResponse.builder()
                .id(20L)
                .workDescription("Analysis")
                .build();
        when(timeRecordService.getTimeRecordsByPeriod(any())).thenReturn(List.of(response));

        String body = """
                {
                  "employeeId": 1,
                  "startTime": "2026-01-01T00:00:00",
                  "endTime": "2026-01-02T00:00:00"
                }
                """;

        mockMvc.perform(post("/api/time-records/period")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(20))
                .andExpect(jsonPath("$[0].workDescription").value("Analysis"));
    }
}
