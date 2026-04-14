package aulouu.timetracker.dto.response;

import aulouu.timetracker.model.Employee;
import aulouu.timetracker.model.Task;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TimeRecordResponse {
    private Long id;
    private EmployeeResponse employee;
    private TaskResponse task;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String workDescription;
}
