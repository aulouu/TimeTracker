package aulouu.timetracker.service;


import aulouu.timetracker.dto.requst.CreateTimeRecordRequest;
import aulouu.timetracker.dto.requst.TimeRecordPeriodRequest;
import aulouu.timetracker.dto.response.TimeRecordResponse;
import aulouu.timetracker.exception.EmployeeNotFoundException;
import aulouu.timetracker.exception.NotValidInputException;
import aulouu.timetracker.exception.TaskNotFoundException;
import aulouu.timetracker.model.Employee;
import aulouu.timetracker.model.Task;
import aulouu.timetracker.model.TimeRecord;
import aulouu.timetracker.repository.EmployeeRepository;
import aulouu.timetracker.repository.TaskRepository;
import aulouu.timetracker.repository.TimeRecordRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeRecordService {
    private final TimeRecordRepository timeRecordRepository;
    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public TimeRecordResponse createTimeRecord(CreateTimeRecordRequest createTimeRecordRequest) {
        if (createTimeRecordRequest.getEndTime() != null && !createTimeRecordRequest.getEndTime().isAfter(createTimeRecordRequest.getStartTime())) {
            throw new NotValidInputException("Дата окончания должна быть позже даты начала");
        }
        Task task = taskRepository.findById(createTimeRecordRequest.getTaskId())
                .orElseThrow(() -> new TaskNotFoundException("Задача с ID " + createTimeRecordRequest.getTaskId() + " не найдена"));
        Employee employee = employeeRepository.findById(createTimeRecordRequest.getEmployeeId())
                .orElseThrow(() -> new EmployeeNotFoundException("Сотрудник с ID " + createTimeRecordRequest.getEmployeeId() + " не найден"));


        TimeRecord timeRecord = modelMapper.map(createTimeRecordRequest, TimeRecord.class);
        timeRecord.setTask(task);
        timeRecord.setEmployee(employee);
        TimeRecord savedTimeRecord = timeRecordRepository.save(timeRecord);
        return modelMapper.map(savedTimeRecord, TimeRecordResponse.class);
    }

    @Transactional(readOnly = true)
    public List<TimeRecordResponse> getTimeRecordsByPeriod(TimeRecordPeriodRequest timeRecordPeriodRequest) {
        if (timeRecordPeriodRequest.getEndTime() != null && !timeRecordPeriodRequest.getEndTime().isAfter(timeRecordPeriodRequest.getStartTime())) {
            throw new NotValidInputException("Дата окончания должна быть позже даты начала");
        }
        if (!employeeRepository.existsById(timeRecordPeriodRequest.getEmployeeId())) {
            throw new EmployeeNotFoundException("Сотрудник с ID " + timeRecordPeriodRequest.getEmployeeId() + " не найден");
        }

        List<TimeRecord> timeRecords = timeRecordRepository.findByEmployeeIdAndStartTimeBetweenOrderByStartTimeDesc(
                timeRecordPeriodRequest.getEmployeeId(),
                timeRecordPeriodRequest.getStartTime(),
                timeRecordPeriodRequest.getEndTime()
        );

        return timeRecords.stream()
                .map(record -> modelMapper.map(record, TimeRecordResponse.class))
                .collect(Collectors.toList());
    }
}
