package aulouu.timetracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aulouu.timetracker.dto.requst.CreateTimeRecordRequest;
import aulouu.timetracker.dto.requst.TimeRecordPeriodRequest;
import aulouu.timetracker.dto.response.TimeRecordResponse;
import aulouu.timetracker.exception.EmployeeNotFoundException;
import aulouu.timetracker.exception.NotValidInputException;
import aulouu.timetracker.exception.TaskNotFoundException;
import aulouu.timetracker.model.Employee;
import aulouu.timetracker.model.Task;
import aulouu.timetracker.model.TaskStatus;
import aulouu.timetracker.model.TimeRecord;
import aulouu.timetracker.repository.EmployeeRepository;
import aulouu.timetracker.repository.TaskRepository;
import aulouu.timetracker.repository.TimeRecordRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TimeRecordServiceTest {

    @Mock
    private TimeRecordRepository timeRecordRepository;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TimeRecordService timeRecordService;

    @Test
    void createTimeRecordShouldThrowWhenRangeInvalid() {
        LocalDateTime now = LocalDateTime.now();
        CreateTimeRecordRequest request = CreateTimeRecordRequest.builder()
                .employeeId(1L)
                .taskId(1L)
                .startTime(now)
                .endTime(now.minusMinutes(1))
                .workDescription("work")
                .build();

        assertThrows(NotValidInputException.class, () -> timeRecordService.createTimeRecord(request));
    }

    @Test
    void createTimeRecordShouldThrowWhenTaskMissing() {
        CreateTimeRecordRequest request = CreateTimeRecordRequest.builder()
                .employeeId(1L)
                .taskId(1L)
                .startTime(LocalDateTime.now().minusHours(2))
                .endTime(LocalDateTime.now().minusHours(1))
                .workDescription("work")
                .build();
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> timeRecordService.createTimeRecord(request));
    }

    @Test
    void createTimeRecordShouldSaveWithTaskAndEmployee() {
        CreateTimeRecordRequest request = CreateTimeRecordRequest.builder()
                .employeeId(2L)
                .taskId(3L)
                .startTime(LocalDateTime.now().minusHours(3))
                .endTime(LocalDateTime.now().minusHours(1))
                .workDescription("implemented endpoint")
                .build();

        Task task = Task.builder().id(3L).title("Task").description("Desc").status(TaskStatus.IN_PROGRESS).build();
        Employee employee = Employee.builder().id(2L).firstName("Ann").lastName("Doe").build();
        TimeRecord mapped = TimeRecord.builder().startTime(request.getStartTime()).endTime(request.getEndTime()).workDescription("implemented endpoint").build();
        TimeRecord saved = mapped.toBuilder().id(7L).task(task).employee(employee).build();
        TimeRecordResponse response = TimeRecordResponse.builder().id(7L).workDescription("implemented endpoint").build();

        when(taskRepository.findById(3L)).thenReturn(Optional.of(task));
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(employee));
        when(modelMapper.map(request, TimeRecord.class)).thenReturn(mapped);
        when(timeRecordRepository.save(mapped)).thenReturn(saved);
        when(modelMapper.map(saved, TimeRecordResponse.class)).thenReturn(response);

        TimeRecordResponse result = timeRecordService.createTimeRecord(request);

        assertEquals(7L, result.getId());
        verify(timeRecordRepository).save(mapped);
        assertEquals(task, mapped.getTask());
        assertEquals(employee, mapped.getEmployee());
    }

    @Test
    void getTimeRecordsByPeriodShouldThrowWhenEmployeeMissing() {
        TimeRecordPeriodRequest request = TimeRecordPeriodRequest.builder()
                .employeeId(100L)
                .startTime(LocalDateTime.now().minusDays(2))
                .endTime(LocalDateTime.now().minusDays(1))
                .build();
        when(employeeRepository.existsById(100L)).thenReturn(false);

        assertThrows(EmployeeNotFoundException.class, () -> timeRecordService.getTimeRecordsByPeriod(request));
    }

    @Test
    void getTimeRecordsByPeriodShouldMapResultList() {
        TimeRecordPeriodRequest request = TimeRecordPeriodRequest.builder()
                .employeeId(9L)
                .startTime(LocalDateTime.of(2026, 1, 1, 0, 0))
                .endTime(LocalDateTime.of(2026, 1, 2, 0, 0))
                .build();
        TimeRecord record = TimeRecord.builder().id(1L).workDescription("analysis").build();
        TimeRecordResponse response = TimeRecordResponse.builder().id(1L).workDescription("analysis").build();

        when(employeeRepository.existsById(9L)).thenReturn(true);
        when(timeRecordRepository.findByEmployeeIdAndStartTimeBetweenOrderByStartTimeDesc(
                request.getEmployeeId(), request.getStartTime(), request.getEndTime())).thenReturn(List.of(record));
        when(modelMapper.map(record, TimeRecordResponse.class)).thenReturn(response);

        List<TimeRecordResponse> result = timeRecordService.getTimeRecordsByPeriod(request);

        assertEquals(1, result.size());
        assertEquals("analysis", result.get(0).getWorkDescription());
    }
}
