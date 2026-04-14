package aulouu.timetracker.service;

import aulouu.timetracker.dto.requst.CreateEmployeeRequest;
import aulouu.timetracker.dto.requst.CreateTaskRequest;
import aulouu.timetracker.dto.response.EmployeeResponse;
import aulouu.timetracker.dto.response.TaskResponse;
import aulouu.timetracker.exception.EmployeeNotFoundException;
import aulouu.timetracker.exception.TaskNotFoundException;
import aulouu.timetracker.model.Employee;
import aulouu.timetracker.model.Task;
import aulouu.timetracker.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Сотрудник с ID " + id + " не найден"));
        return modelMapper.map(employee, EmployeeResponse.class);
    }

    @Transactional
    public EmployeeResponse createEmployee(CreateEmployeeRequest createEmployeeRequest) {
        Employee employee = modelMapper.map(createEmployeeRequest, Employee.class);
        Employee savedEmployee = employeeRepository.save(employee);
        return modelMapper.map(savedEmployee, EmployeeResponse.class);
    }
}
