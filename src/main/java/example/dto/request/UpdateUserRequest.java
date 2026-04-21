package example.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateUserRequest {
    @Email(message = "Email is invalid")
    private String email;

    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Login can only contain letters, numbers, and underscore")
    private String login;
    private String name;
    @Past(message = "Birthday must be in the Past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;


    public boolean hasEmail() {
        return email != null && !email.isBlank();
    }
    public boolean hasLogin() {
        return login != null && !login.isBlank();
    }
    public boolean hasName() {
        return name != null && !name.isBlank();
    }
    public boolean hasBirthday() {
        return birthday != null;
    }
}
