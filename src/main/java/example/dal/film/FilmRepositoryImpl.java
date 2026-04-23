package example.dal.film;

import example.dal.BaseRepository;
import example.model.Film;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FilmRepositoryImpl extends BaseRepository<Film> implements FilmRepository {
    public FilmRepositoryImpl(JdbcTemplate jdbc, RowMapper<Film> rowMapper) {
        super(jdbc, rowMapper);
    }

    private static final String INSERT_FILM = "INSERT INTO films (film_name, film_description, film_release_date, " +
            "film_duration, film_mpa_rating_id) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_FILM = "UPDATE films SET film_name = ?, film_description = ?, film_release_date = ?, " +
            "film_duration = ?, film_mpa_rating_id = ? WHERE film_id = ?";
    private static final String DELETE_FILM = "DELETE FROM films WHERE film_id = ?";
    private static final String FIND_BY_ID = "SELECT * FROM films WHERE film_id = ?";
    private static final String FIND_ALL = "SELECT * FROM films";
    private static final String EXISTS_BY_ID = "SELECT 1 FROM films WHERE film_id = ? LIMIT 1";




    @Override
    public Film save(Film film) {
        Long mpaId = film.getMpa() != null ? film.getMpa().getId() : null;
        long id = insert(
                INSERT_FILM,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                mpaId
        );
        film.setId(id);
        return film;
    }

    @Override
    public Film update(Film film) {
        update (
            UPDATE_FILM,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpaId(),
                film.getId()
        );
        return film;
    }

    @Override
    public void delete(Long id) {
        delete(DELETE_FILM, id);
    }

    @Override
    public Optional<Film> findById(Long id) {
        return findOne(FIND_BY_ID, id);
    }

    @Override
    public List<Film> findAll() {
        return findAll(FIND_ALL);
    }

    @Override
    public boolean existsById(Long id) {
        return exist(EXISTS_BY_ID, id);
    }
}
