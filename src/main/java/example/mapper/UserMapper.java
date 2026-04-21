package example.mapper;

import example.dto.request.CreateUserRequest;
import example.dto.request.UpdateUserRequest;
import example.dto.response.UserResponse;
import example.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashSet;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    // поставил заглушку на списке друзей, в следующий раз вытащу друзей из отдельного репозитория
    public static UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                new HashSet<>()
        );
    }


    public static User toUser(CreateUserRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setLogin(request.getLogin());
        user.setName(request.getName());
        user.setBirthday(request.getBirthday());
        return user;
    }


    public static User toUserUpdate(UpdateUserRequest request, User user) {
        if(request.hasEmail()) {
            user.setEmail(request.getEmail());
        }
        if(request.hasLogin()) {
            user.setLogin(request.getLogin());
        }
        if(request.hasName()) {
            user.setName(request.getName());
        }
        if(request.hasBirthday()) {
            user.setBirthday(request.getBirthday());
        }

        return user;
    }
}
