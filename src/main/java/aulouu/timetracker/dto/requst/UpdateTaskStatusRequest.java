package aulouu.timetracker.dto.requst;

import aulouu.timetracker.model.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTaskStatusRequest {
    @NotNull(message = "Выберите статус задачи: NEW, IN_PROGRESS, DONE")
    private TaskStatus status;
}
