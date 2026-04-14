package aulouu.timetracker.dto.response;

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
    private Long employeeId;
    private Task task;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String workDescription;
}
