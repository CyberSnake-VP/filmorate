package example.service;


import example.dto.request.CreateUserRequest;
import example.dto.request.UpdateUserRequest;
import example.dto.response.UserResponse;
import example.model.User;

import java.util.List;

public interface UserService {
    UserResponse create(CreateUserRequest request);
    UserResponse get(Long id);
    List<UserResponse> getAll();
    UserResponse update(UpdateUserRequest request, Long id);
    void delete(Long id);
}
