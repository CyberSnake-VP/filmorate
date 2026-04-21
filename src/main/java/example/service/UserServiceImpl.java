package example.service;

import example.dal.UserRepository;
import example.dto.request.CreateUserRequest;
import example.dto.request.UpdateUserRequest;
import example.dto.response.UserResponse;
import example.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Override
    public UserResponse create(CreateUserRequest request) {
        return null;
    }

    @Override
    public UserResponse get(Long id) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return List.of();
    }

    @Override
    public UserResponse update(UpdateUserRequest request, Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
