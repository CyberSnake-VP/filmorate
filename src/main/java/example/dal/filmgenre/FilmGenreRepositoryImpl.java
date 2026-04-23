package example.dal.filmgenre;

import example.model.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class FilmGenreRepositoryImpl implements FilmGenreRepository {
    private final JdbcTemplate jdbc;

    private static final String DELETE_GENRE_BY_FILM_QUERY = "DELETE FROM film_genres WHERE film_id = ?";
    private static final String ADD_GENRE_TO_FILM_QUERY = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";


    @Override
    public void addGenresToFilm(Long filmId, List<Long> genreIds) {
        jdbc.batchUpdate(ADD_GENRE_TO_FILM_QUERY, genreIds, genreIds.size(), (ps, genreId) -> {
                    ps.setLong(1, filmId);
                    ps.setLong(2, genreId);
                }
        );

    }

    @Override
    public void deleteGenreByFilm(Long id) {
        jdbc.update(DELETE_GENRE_BY_FILM_QUERY, id);
    }

    // получает список id жанров для списка id фильмов
    @Override
    public Map<Long, List<Long>> getFilmGenres(List<Long> filmIds) {

        if (filmIds == null || filmIds.isEmpty()) {
            return Collections.emptyMap();
        }

        String placeholder = String.join(", ", Collections.nCopies(filmIds.size(), "?"));

        String sql = "SELECT film_id, genre_id FROM film_genres WHERE film_id IN (" + placeholder + ")";

        Map<Long, List<Long>> response = jdbc.query(sql, rs -> {
            Map<Long, List<Long>> result = new HashMap<>();
            while (rs.next()) {
                Long filmId = rs.getLong("film_id");
                Long genreId = rs.getLong("genre_id");
                result.computeIfAbsent(filmId, k -> new ArrayList<>()).add(genreId);
            }
            return result;
        }, filmIds.toArray());

        return response;
    }
}
