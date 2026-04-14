package aulouu.timetracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aulouu.timetracker.dto.requst.CreateTaskRequest;
import aulouu.timetracker.dto.requst.UpdateTaskStatusRequest;
import aulouu.timetracker.dto.response.TaskResponse;
import aulouu.timetracker.exception.TaskNotFoundException;
import aulouu.timetracker.model.Task;
import aulouu.timetracker.model.TaskStatus;
import aulouu.timetracker.repository.TaskRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTaskShouldSaveAndReturnResponse() {
        CreateTaskRequest request = CreateTaskRequest.builder()
                .title("Task")
                .description("Description")
                .status(TaskStatus.NEW)
                .build();
        Task entity = Task.builder().title("Task").description("Description").status(TaskStatus.NEW).build();
        Task saved = entity.toBuilder().id(1L).build();
        TaskResponse response = new TaskResponse(1L, "Task", "Description", TaskStatus.NEW);

        when(modelMapper.map(request, Task.class)).thenReturn(entity);
        when(taskRepository.save(entity)).thenReturn(saved);
        when(modelMapper.map(saved, TaskResponse.class)).thenReturn(response);

        TaskResponse result = taskService.createTask(request);

        assertEquals(1L, result.getId());
        assertEquals(TaskStatus.NEW, result.getStatus());
        verify(taskRepository).save(entity);
    }

    @Test
    void getTaskByIdShouldThrowWhenNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(99L));
    }

    @Test
    void updateTaskStatusShouldPersistNewStatus() {
        Task existing = Task.builder().id(5L).title("T").description("D").status(TaskStatus.NEW).build();
        UpdateTaskStatusRequest request = UpdateTaskStatusRequest.builder().status(TaskStatus.DONE).build();
        Task updated = existing.toBuilder().status(TaskStatus.DONE).build();
        TaskResponse response = new TaskResponse(5L, "T", "D", TaskStatus.DONE);

        when(taskRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(taskRepository.save(any(Task.class))).thenReturn(updated);
        when(modelMapper.map(updated, TaskResponse.class)).thenReturn(response);

        TaskResponse result = taskService.updateTaskStatus(5L, request);

        assertEquals(TaskStatus.DONE, result.getStatus());
        verify(taskRepository).save(existing);
    }
}
