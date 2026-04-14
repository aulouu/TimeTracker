package aulouu.timetracker.dto.requst;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TimeRecordPeriodRequest {
    @NotNull(message = "Укажите ID сотрудника")
    @Positive(message = "ID сотрудника должен быть положительным числом")
    private Long employeeId;

    @NotNull(message = "Укажите дату начала периода")
    private LocalDateTime startTime;

    @NotNull(message = "Укажите дату окончания периода")
    private LocalDateTime endTime;
}
