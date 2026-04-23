package example.dto.request.film;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.List;

public record UpdateFilmRequest(
        String name,
        String description,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate releaseDate,
        @Positive(message = "Duration must be a positive number")
        Integer duration,
        List<Long> genreIds,
        @Positive(message = "MPA rating must be a positive number")
        Long mpaId
) {
    public boolean hasName() {
        return name != null && !name.isBlank();
    }
    public boolean hasDescription() {
        return description != null && !description.isBlank();
    }
    public boolean hasReleaseDate() {
        return releaseDate != null;
    }
    public boolean hasDuration() {
        return duration != null;
    }
    public boolean hasGenreIds() {
        return genreIds != null && !genreIds.isEmpty();
    }
    public boolean hasMpaId() {
        return mpaId != null;
    }
}
