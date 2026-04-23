package example.service;


import example.dto.request.user.CreateUserRequest;
import example.dto.request.user.UpdateUserRequest;
import example.dto.response.user.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse create(CreateUserRequest request);
    UserResponse get(Long id);
    List<UserResponse> getAll();
    UserResponse update(UpdateUserRequest request, Long id);
    void delete(Long id);

    void addFriend(Long userId, Long friendId);
    void removeFriend(Long userId, Long friendId);
    List<UserResponse> getFriends(Long userId);
    List<UserResponse> getCommonFriends(Long userId, Long friendId);

}
