package example.dal;

import java.util.List;
import java.util.Map;

public interface FriendRepository {
    void addFriend(Long userId, Long friendId);
    List<Long> getFriends(Long userId);
    boolean existFriend(Long userId, Long friendId);
    void removeFriend(Long userId, Long friendId);
    Map<Long, List<Long>> getFriendsForUsers(List<Long> userIds);

}
