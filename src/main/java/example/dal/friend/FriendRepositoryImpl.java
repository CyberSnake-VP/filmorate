package example.dal.friend;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class FriendRepositoryImpl implements FriendRepository {
    private final JdbcTemplate jdbcTemplate;

    private static final String INSERT_FRIEND_QUERY = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";
    private static final String GET_FRIENDS_IDS_BY_USER_ID_QUERY = "SELECT friend_id FROM friends WHERE user_id = ?";
    private static final String EXISTS_FRIEND_BY_USER_ID_AND_FRIEND_ID_QUERY = "SELECT EXISTS (SELECT 1 FROM friends WHERE user_id = ? AND friend_id = ? LIMIT 1)";
    private static final String REMOVE_FRIEND_QUERY = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";


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

    @Override
    public void removeFriend(Long userId, Long friendId) {
        jdbcTemplate.update(REMOVE_FRIEND_QUERY, userId, friendId);
    }

    @Override
    public Map<Long, List<Long>> getFriendsForUsers(List<Long> userIds) {

        if (userIds == null || userIds.isEmpty()) {
            return new HashMap<>();
        }

        // получаем плейсхолдеры для формирования запроса
        String placeholders = String.join(", ", Collections.nCopies(userIds.size(), "?"));

        // получаем список друзей пользователя
        String sql = String.format("SELECT user_id, friend_id FROM friends WHERE user_id IN (%s)", placeholders);

        return jdbcTemplate.query(sql, rs -> {
            Map<Long, List<Long>> result = new HashMap<>();
            while (rs.next()) {
                Long userId = rs.getLong("user_id");
                Long friendId = rs.getLong("friend_id");
                result.computeIfAbsent(userId, k -> new ArrayList<>()).add(friendId);
            }
            return result;
        }, userIds.toArray());
    }

}
