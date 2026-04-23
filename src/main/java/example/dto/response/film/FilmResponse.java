package example.dto.response.film;

import com.fasterxml.jackson.annotation.JsonInclude;
import example.model.Genre;
import example.model.MpaRating;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FilmResponse(
        Long id,
        String name,
        String description,
        LocalDate releaseDate,
        Integer duration,
        Set<Long> likes,
        List<Genre> genres,
        MpaRating mpa
) {
}
