package aulouu.timetracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aulouu.timetracker.dto.requst.CreateEmployeeRequest;
import aulouu.timetracker.dto.response.EmployeeResponse;
import aulouu.timetracker.exception.EmployeeNotFoundException;
import aulouu.timetracker.model.Employee;
import aulouu.timetracker.repository.EmployeeRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void getEmployeeByIdShouldThrowWhenMissing() {
        when(employeeRepository.findById(77L)).thenReturn(Optional.empty());
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(77L));
    }

    @Test
    void createEmployeeShouldSaveAndReturnResponse() {
        CreateEmployeeRequest request = CreateEmployeeRequest.builder().firstName("Ann").lastName("Doe").build();
        Employee entity = Employee.builder().firstName("Ann").lastName("Doe").build();
        Employee saved = Employee.builder().id(1L).firstName("Ann").lastName("Doe").build();
        EmployeeResponse response = EmployeeResponse.builder().id(1L).firstName("Ann").lastName("Doe").build();

        when(modelMapper.map(request, Employee.class)).thenReturn(entity);
        when(employeeRepository.save(entity)).thenReturn(saved);
        when(modelMapper.map(saved, EmployeeResponse.class)).thenReturn(response);

        EmployeeResponse result = employeeService.createEmployee(request);

        assertEquals(1L, result.getId());
        verify(employeeRepository).save(entity);
    }
}
