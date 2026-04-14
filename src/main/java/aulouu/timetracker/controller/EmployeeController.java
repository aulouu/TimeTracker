package aulouu.timetracker.controller;

import aulouu.timetracker.dto.requst.CreateEmployeeRequest;
import aulouu.timetracker.dto.requst.CreateTaskRequest;
import aulouu.timetracker.dto.requst.UpdateTaskStatusRequest;
import aulouu.timetracker.dto.response.EmployeeResponse;
import aulouu.timetracker.dto.response.TaskResponse;
import aulouu.timetracker.service.EmployeeService;
import aulouu.timetracker.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Validated
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/{id}")
    public EmployeeResponse getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping
    public EmployeeResponse createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
        return employeeService.createEmployee(request);
    }
}
