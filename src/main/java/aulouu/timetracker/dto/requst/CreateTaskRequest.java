package aulouu.timetracker.dto.requst;

import aulouu.timetracker.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateTaskRequest {
    @NotBlank(message = "Укажите название задачи")
    @Size(min = 1, max = 200, message = "Название — от 1 до 200 символов")
    private String title;

    @NotBlank(message = "Укажите описание задачи")
    @Size(min = 1, max = 2000, message = "Описание — до 2000 символов")
    private String description;

    @NotNull(message = "Выберите статус задачи: NEW, IN_PROGRESS, DONE")
    private TaskStatus status;
}
