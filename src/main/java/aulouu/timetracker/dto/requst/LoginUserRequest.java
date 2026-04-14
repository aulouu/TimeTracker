package aulouu.timetracker.dto.requst;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserRequest {
    @NotBlank(message = "Укажите username")
    @Size(min = 6, max = 20)
    private String username;

    @NotBlank(message = "Укажите пароль")
    @Size(min = 6, max = 20)
    private String password;
}
