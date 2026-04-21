package example.dal;

import java.util.List;

public interface FriendRepository {
    void addFriend(Long userId, Long friendId);
    List<Long> getFriends(Long userId);
    boolean existFriend(Long userId, Long friendId);
}
