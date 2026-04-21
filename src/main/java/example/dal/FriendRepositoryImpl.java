package example.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FriendRepositoryImpl implements FriendRepository{
    private final JdbcTemplate jdbcTemplate;

    private static final String INSERT_FRIEND_QUERY = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";
    private static final String GET_FRIENDS_IDS_BY_USER_ID_QUERY = "SELECT friend_id FROM friends WHERE user_id = ?";
    private static final String EXISTS_FRIEND_BY_USER_ID_AND_FRIEND_ID_QUERY = "SELECT EXISTS (SELECT 1 FROM friends WHERE user_id = ? AND friend_id = ? LIMIT 1)";

    @Override
    public void addFriend(Long userId, Long friendId) {
        jdbcTemplate.update(INSERT_FRIEND_QUERY, userId, friendId);
    }

    @Override
    public List<Long> getFriends(Long userId) {
        return jdbcTemplate.query(
                GET_FRIENDS_IDS_BY_USER_ID_QUERY,
                (rs, rowNum) -> rs.getLong("friend_id"),
                userId
        );
    }

    @Override
    public boolean existFriend(Long userId, Long friendId) {
       Boolean exists = jdbcTemplate.queryForObject(
               EXISTS_FRIEND_BY_USER_ID_AND_FRIEND_ID_QUERY,
               Boolean.class,
               userId,
               friendId
       );
       // безопасно, если вдруг queryForObject вернет null
       return Boolean.TRUE.equals(exists);
    }

}
