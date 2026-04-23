package example.dal.user;

import example.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User create(User user);
    List<User> findAll();
    Optional<User> findOne(Long id);
    User update(User user);
    boolean delete(Long id);
    boolean existById(Long id);
    boolean existLogin(String login);
    boolean existEmail(String email);
    List<User> getCommonFriends(Long userId, Long friendId);
    List<User> findAllByIds(List<Long> ids);
}
