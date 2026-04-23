package example.dal.filmlikes;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FilmLikesRepositoryImpl implements FIlmLikesRepository {

    private static final String ADD_LIKE_QUERY = "INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)";
    private static final String REMOVE_LIKE_QUERY = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
    private static final String GET_LIKES_QUERY = "SELECT user_id FROM film_likes WHERE film_id = ?";
    private static final String IS_LIKED_QUERY = "SELECT 1 FROM film_likes WHERE film_id = ? AND user_id = ? LIMIT 1";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(Long filmId, Long userId) {
        if (filmId != null && userId != null) {
            jdbcTemplate.update(ADD_LIKE_QUERY, filmId, userId);
        }
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        if (filmId != null && userId != null) {
            jdbcTemplate.update(REMOVE_LIKE_QUERY, filmId, userId);
        }
    }

    @Override
    public List<Long> getLikes(Long filmId) {
        if(filmId == null) {
            return Collections.emptyList();
        }

        return jdbcTemplate.query(GET_LIKES_QUERY,
                (rs, rowNum) -> rs.getLong("user_id"), filmId);
    }

    @Override
    public boolean isLiked(Long filmId, Long userId) {
        if(filmId != null && userId != null) {
            try {
                jdbcTemplate.queryForObject(IS_LIKED_QUERY, Integer.class, filmId, userId);
                return true;
            } catch (EmptyResultDataAccessException e) {
                return false;
            }
        }
        return false;
    }
}
