package example.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CreateUserRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Email not valid")
    private String email;

    @NotBlank(message = "Login is required")
    @Size(min = 3, max = 20, message = "Login must be between 3 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Login can only contain letters, numbers and underscore")
    private String login;

    private String name;

    @Past(message = "Birthday must be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
}
