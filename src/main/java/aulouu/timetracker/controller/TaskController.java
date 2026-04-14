package aulouu.timetracker.controller;

import aulouu.timetracker.dto.requst.CreateTaskRequest;
import aulouu.timetracker.dto.requst.UpdateTaskStatusRequest;
import aulouu.timetracker.dto.response.TaskResponse;
import aulouu.timetracker.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Validated
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PostMapping
    public TaskResponse createTask(@Valid @RequestBody CreateTaskRequest request) {
        return taskService.createTask(request);
    }

    @PutMapping("/{id}/status")
    public TaskResponse updateTaskStatus(@PathVariable Long id,
                                         @Valid @RequestBody UpdateTaskStatusRequest request) {
        return taskService.updateTaskStatus(id, request);
    }
}
