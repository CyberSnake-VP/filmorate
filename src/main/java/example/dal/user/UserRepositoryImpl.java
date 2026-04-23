package example.dal.user;

import example.dal.BaseRepository;
import example.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl extends BaseRepository<User> implements UserRepository{
    public UserRepositoryImpl(JdbcTemplate jdbc, RowMapper<User> rowMapper) {
        super(jdbc, rowMapper);
    }

    private static final String INSERT_QUERY = "INSERT INTO users (email, login, name, birthday)" +
            " VALUES (?, ?, ?, ?)";
    private static final String GET_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String GET_ALL_QUERY = "SELECT * FROM users";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ?" +
            " WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM users WHERE id = ?";
    private static final String EXIST_BY_ID_QUERY = "SELECT 1 FROM users WHERE id = ? LIMIT 1";
    private static final String EXIST_EMAIL_QUERY = "SELECT 1 FROM users WHERE email = ? LIMIT 1";
    private static final String EXIST_LOGIN_QUERY = "SELECT 1 FROM users WHERE login = ? LIMIT 1";
    private static final String FIND_COMMON_FRIENDS = "SELECT * FROM users WHERE id IN (" +
            "SELECT friend_id FROM friends WHERE user_id = ? " +
            "INTERSECT " +
            "SELECT friend_id FROM friends WHERE user_id = ? )";




    @Override
    public User create(User user) {
        long id = insert(
                INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }

    @Override
    public List<User> findAll() {
        return findAll(GET_ALL_QUERY);
    }

    @Override
    public Optional<User> findOne(Long id) {
        return findOne(GET_BY_ID_QUERY, id);
    }

    @Override
    public User update(User user) {
        update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    @Override
    public boolean delete(Long id) {
        return delete(DELETE_QUERY, id);
    }

    @Override
    public boolean isExistById(Long id) {
        return exist(EXIST_BY_ID_QUERY, id);
    }

    @Override
    public boolean isLoginExist(String login) {
        return exist(EXIST_LOGIN_QUERY, login);
    }

    @Override
    public boolean isEmailExist(String email) {
        return exist(EXIST_EMAIL_QUERY, email);
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long friendId) {
        return findAll(FIND_COMMON_FRIENDS, userId, friendId);
    }

    // НУЖНО ПОЛУЧИТЬ ДАННЫЕ С ОДНОГО ЗАПРОСА, ПОЭТОМУ СОБИРАЕМ id из списка в строку "?, ?, ?"
    @Override
    public List<User> findAllByIds(List<Long> friendIds) {

        if (friendIds.isEmpty()) {
            return Collections.emptyList();
        }

        // создаем placeholders
        String placeholders = String.join(", ",  Collections.nCopies(friendIds.size(),"?"));

        String sql = String.format("SELECT * FROM users WHERE id IN (%s)", placeholders);

        return findAll(sql, friendIds.toArray());
    }

}
