package example.dal;

import example.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User create(User user);
    List<User> findAll();
    Optional<User> findOne(Long id);
    User update(User user);
    boolean delete(Long id);
    boolean isExistById(Long id);
    boolean isLoginExist(String login);
    boolean isEmailExist(String email);
    List<User> getCommonFriends(Long userId, Long friendId);
}
