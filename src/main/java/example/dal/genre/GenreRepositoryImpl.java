package example.dal.genre;

import example.dal.BaseRepository;
import example.model.Genre;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepositoryImpl extends BaseRepository<Genre> implements GenreRepository{
    public GenreRepositoryImpl(JdbcTemplate jdbc, RowMapper<Genre> rowMapper) {
        super(jdbc, rowMapper);
    }

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genres WHERE genre_id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM genres";
    private static final String FIND_ALL_BY_FILM_ID_QUERY = "SELECT * FROM genres WHERE genre_id IN (" +
            "SELECT genre_id FROM film_genres WHERE film_id = ?)";

    @Override
    public Optional<Genre> findById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public List<Genre> findAll() {
        return findAll(FIND_ALL_QUERY);
    }

    @Override
    public List<Genre> findByFilmId(Long id) {
        return findAll(FIND_ALL_BY_FILM_ID_QUERY, id);
    }

    @Override
    public List<Genre> findAllByIds(List<Long> ids) {

        String placeholders = String.join(", ", Collections.nCopies(ids.size(), "?"));

        String sql = "SELECT * FROM genres WHERE genre_id IN (" + placeholders + ")";

        return findAll(sql, ids.toArray());
    }
}
