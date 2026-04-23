package example.mapper;


import example.dto.request.film.CreateFilmRequest;
import example.dto.request.film.UpdateFilmRequest;
import example.dto.response.film.FilmResponse;
import example.model.Film;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmMapper {

    public static FilmResponse toFilmResponse(Film film) {
        return new FilmResponse(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getLikes(),
                film.getGenres(),
                film.getMpa()
        );
    }


    public static Film toFilm(CreateFilmRequest request) {
        Film film = new Film();
        film.setName(request.name());
        film.setDescription(request.description());
        film.setReleaseDate(request.releaseDate());
        film.setDuration(request.duration());
        return film;
    }

    public static Film toFilmUpdate(UpdateFilmRequest request, Film film) {
        if(request.hasName()) {
            film.setName(request.name());
        }
        if(request.hasDescription()) {
            film.setDescription(request.description());
        }
        if(request.hasReleaseDate()) {
            film.setReleaseDate(request.releaseDate());
        }
        if(request.hasDuration()) {
            film.setDuration(request.duration());
        }

        return film;
    }
}
