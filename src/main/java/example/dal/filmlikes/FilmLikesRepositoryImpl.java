package example.dal.filmlikes;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

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
    public Map<Long, Set<Long>> getLikesForFilms(List<Long> filmIds) {
        if(filmIds == null) {
            return Collections.emptyMap();
        }

        String placeholder = String.join(", ", Collections.nCopies(filmIds.size(), "?"));
        String sql = "SELECT film_id, user_id FROM film_likes WHERE film_id IN (" + placeholder + ")";


        return jdbcTemplate.query(sql, rs -> {
            Map<Long, Set<Long>> result = new HashMap<>();
            while (rs.next()) {
                Long filmId = rs.getLong("film_id");
                Long userId = rs.getLong("user_id");
                result.computeIfAbsent(filmId, k -> new HashSet<>()).add(userId);
            }
            return result;
        }, filmIds.toArray());

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
