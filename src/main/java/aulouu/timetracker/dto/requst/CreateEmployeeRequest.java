package aulouu.timetracker.dto.requst;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeeRequest {
    @NotBlank(message = "Укажите имя")
    @Size(min = 1, max = 200, message = "Имя — от 1 до 200 символов")
    private String firstName;

    @NotBlank(message = "Укажите фамилию")
    @Size(min = 1, max = 200, message = "Фамилия — от 1 до 200 символов")
    private String lastName;
}
