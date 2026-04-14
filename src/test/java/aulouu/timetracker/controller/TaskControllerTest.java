package aulouu.timetracker.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import aulouu.timetracker.dto.response.TaskResponse;
import aulouu.timetracker.model.TaskStatus;
import aulouu.timetracker.security.jwt.JwtAuthEntryPoint;
import aulouu.timetracker.security.jwt.JwtAuthTokenFilter;
import aulouu.timetracker.security.service.AuthUserDetailsService;
import aulouu.timetracker.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;
    @MockBean
    private JwtAuthTokenFilter jwtAuthTokenFilter;
    @MockBean
    private JwtAuthEntryPoint jwtAuthEntryPoint;
    @MockBean
    private AuthUserDetailsService authUserDetailsService;

    @Test
    void getTaskByIdShouldReturnTask() throws Exception {
        TaskResponse response = new TaskResponse(5L, "Task", "Desc", TaskStatus.NEW);
        when(taskService.getTaskById(5L)).thenReturn(response);

        mockMvc.perform(get("/api/tasks/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.status").value("NEW"));
    }

    @Test
    void createTaskShouldReturnTask() throws Exception {
        TaskResponse response = new TaskResponse(6L, "Task 2", "Desc 2", TaskStatus.IN_PROGRESS);
        when(taskService.createTask(any())).thenReturn(response);
        String body = """
                {"title":"Task 2","description":"Desc 2","status":"IN_PROGRESS"}
                """;

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(6))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void updateTaskStatusShouldReturnBadRequestWhenInvalid() throws Exception {
        String body = """
                {"status":null}
                """;

        mockMvc.perform(put("/api/tasks/6/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }
}
