package example.mapper;

import example.dto.request.CreateUserRequest;
import example.dto.request.UpdateUserRequest;
import example.dto.response.UserResponse;
import example.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


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
                user.getFriends()
        );
    }


    public static User toUser(CreateUserRequest request) {
        User user = new User();
        user.setEmail(request.email());
        user.setLogin(request.login());
        user.setName(request.name());
        user.setBirthday(request.birthday());
        return user;
    }


    public static User toUserUpdate(UpdateUserRequest request, User user) {
        if(request.hasEmail()) {
            user.setEmail(request.email());
        }
        if(request.hasLogin()) {
            user.setLogin(request.login());
        }
        if(request.hasName()) {
            user.setName(request.name());
        }
        if(request.hasBirthday()) {
            user.setBirthday(request.birthday());
        }

        return user;
    }
}
