package example.mapper;

import example.dto.response.GenreResponse;
import example.model.Genre;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenreMapper {

    public static GenreResponse toGenreResponse(Genre genre) {
        return new GenreResponse(
                genre.getId(),
                genre.getName()
        );
    }
}
