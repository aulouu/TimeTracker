package aulouu.timetracker.dto.response;

import jakarta.persistence.Column;
import lombok.*;

@Data
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {
    private Long id;
    private String firstName;
    private String lastName;
}
