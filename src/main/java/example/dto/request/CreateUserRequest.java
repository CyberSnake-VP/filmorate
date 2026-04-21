package example.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDate;


public record CreateUserRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Email not valid")
        String email,

        @NotBlank(message = "Login is required")
        @Size(min = 3, max = 20, message = "Login must be between 3 and 20 characters")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Login can only contain letters, numbers and underscore")
        String login,

        String name,

        @Past(message = "Birthday must be in the past")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate birthday
) {
}
