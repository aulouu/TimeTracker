package aulouu.timetracker.service;

import aulouu.timetracker.dto.requst.CreateTaskRequest;
import aulouu.timetracker.dto.requst.UpdateTaskStatusRequest;
import aulouu.timetracker.dto.response.TaskResponse;
import aulouu.timetracker.exception.TaskNotFoundException;
import aulouu.timetracker.model.Task;
import aulouu.timetracker.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Задача с ID " + id + " не найдена"));
        return modelMapper.map(task, TaskResponse.class);
    }

    @Transactional
    public TaskResponse createTask(CreateTaskRequest createTaskRequest) {
        Task task = modelMapper.map(createTaskRequest, Task.class);
        Task savedTask = taskRepository.save(task);
        return modelMapper.map(savedTask, TaskResponse.class);
    }

    @Transactional
    public TaskResponse updateTaskStatus(Long id, UpdateTaskStatusRequest updateTaskStatusRequest) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Задача с ID " + id + " не найдена"));
        task.setStatus(updateTaskStatusRequest.getStatus());
        Task updatedTask = taskRepository.save(task);
        return modelMapper.map(updatedTask, TaskResponse.class);
    }
}
