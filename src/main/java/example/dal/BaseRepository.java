package example.dal;

import example.exception.InternalServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class BaseRepository<T> {
    private static final String UPDATE_ERROR_MESSAGE = "Не удалось обновить данные";
    private static final String INSERT_ERROR_MESSAGE = "Не удалось сохранить данные";

    protected final JdbcTemplate jdbc;
    protected final RowMapper<T> rowMapper;


    protected Optional<T> findOne(String query, Object... args) {
        try {
            T result = jdbc.queryForObject(query, rowMapper, args);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    protected List<T> findAll(String query, Object... args) {
        return jdbc.query(query, rowMapper, args);
    }

    protected void update(String query, Object... args) {
        int count = jdbc.update(query, args);
        if (count == 0) {
            throw new InternalServerException(UPDATE_ERROR_MESSAGE);
        }
    }

    protected long insert(String query, Object... args) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKeyAs(Long.class);
        if(id == null) {
            throw new InternalServerException(INSERT_ERROR_MESSAGE);
        }
        return id;
    }

    protected boolean exist(String query, Object... args) {
        try {
            jdbc.queryForObject(query, Integer.class, args);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    protected boolean delete(String query, Object... args) {
        int row = jdbc.update(query, args);
        return row > 0;
    }
}
