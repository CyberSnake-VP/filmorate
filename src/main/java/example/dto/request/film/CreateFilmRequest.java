package example.dto.request.film;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.List;

public record CreateFilmRequest(
        @NotBlank(message = "Name is required")
        String name,

        String description,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate releaseDate,

        @Positive(message = "Duration must be a positive number")
        Integer duration,

        List<Long> genreIds,

        Long mpaId
) {
}
