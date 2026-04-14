package aulouu.timetracker.dto.requst;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateTimeRecordRequest {
    @NotNull(message = "Укажите ID сотрудника")
    @Positive(message = "ID сотрудника должен быть положительным числом")
    private Long employeeId;

    @NotNull(message = "укажите ID задачи")
    @Positive(message = "ID задачи должен быть положительным числом")
    private Long taskId;

    @NotNull(message = "Укажите время начала")
    private LocalDateTime startTime;

    @NotNull(message = "Укажите время окончания")
    @Future(message = "Время окончания не может быть в прошлом")
    private LocalDateTime endTime;

    @NotBlank(message = "Укажите описание работы")
    @Size(max = 500, message = "Описание работы не может превышать 500 символов")
    private String workDescription;
}
