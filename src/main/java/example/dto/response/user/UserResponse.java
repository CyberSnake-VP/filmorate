package example.dto.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponse(
        Long id,
        String email,
        String login,
        String name,
        LocalDate birthday,

        Set<Long> friends) {

}
