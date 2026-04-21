package example.dal.mappers;


import example.model.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

@Component
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setEmail(rs.getString("email"));
        user.setLogin(rs.getString("login"));
        user.setName(rs.getString("name"));
        Date birthday = rs.getDate("birthday");
        if(birthday != null) {
            user.setBirthday(birthday.toLocalDate());
        }

        // ЗАГЛУШКА ЗДЕСЬ
        user.setFriends(new HashSet<>());  // пустой список друзей
        return user;
    }
}
