package aulouu.timetracker.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import aulouu.timetracker.model.Employee;
import aulouu.timetracker.model.Task;
import aulouu.timetracker.model.TaskStatus;
import aulouu.timetracker.model.TimeRecord;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@Testcontainers(disabledWithoutDocker = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RepositoryIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
    }

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private TimeRecordRepository timeRecordRepository;

    @Test
    void taskRepositoryShouldSaveAndFindTask() {
        Task task = Task.builder()
                .title("Implement endpoint")
                .description("Create endpoint")
                .status(TaskStatus.NEW)
                .build();

        Task saved = taskRepository.save(task);

        assertTrue(taskRepository.findById(saved.getId()).isPresent());
    }

    @Test
    void timeRecordRepositoryShouldReturnRecordsByEmployeeAndPeriod() {
        Employee employee = employeeRepository.save(Employee.builder().firstName("Ann").lastName("Doe").build());
        Task task = taskRepository.save(Task.builder()
                .title("Task")
                .description("Description")
                .status(TaskStatus.IN_PROGRESS)
                .build());
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 1, 11, 30);

        timeRecordRepository.save(TimeRecord.builder()
                .employee(employee)
                .task(task)
                .startTime(start)
                .endTime(end)
                .workDescription("Work")
                .build());

        List<TimeRecord> records = timeRecordRepository.findByEmployeeIdAndStartTimeBetweenOrderByStartTimeDesc(
                employee.getId(),
                LocalDateTime.of(2026, 1, 1, 0, 0),
                LocalDateTime.of(2026, 1, 2, 0, 0)
        );

        assertEquals(1, records.size());
        assertEquals("Work", records.get(0).getWorkDescription());
    }
}
