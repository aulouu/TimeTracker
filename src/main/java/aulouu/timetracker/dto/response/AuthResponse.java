package aulouu.timetracker.dto.response;

import lombok.*;

@Data
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private final String tokenType = "Bearer ";
    private String username;
    private String token;
}
