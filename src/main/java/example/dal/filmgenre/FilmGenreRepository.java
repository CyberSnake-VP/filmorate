package example.dal.filmgenre;


import java.util.List;
import java.util.Map;

public interface FilmGenreRepository {
    void addGenresToFilm(Long id, List<Long> genreIds);
    void deleteGenreByFilm(Long id);
    Map<Long, List<Long>> getFilmGenres(List<Long> filmIds);
}
