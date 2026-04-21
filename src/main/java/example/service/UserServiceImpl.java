package example.service;

import example.dal.UserRepository;
import example.dto.request.CreateUserRequest;
import example.dto.request.UpdateUserRequest;
import example.dto.response.UserResponse;
import example.exception.ConditionNotMetException;
import example.exception.NotFoundException;
import example.mapper.UserMapper;
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

    private static final String LOGIN_IS_EXISTING = "Логин уже занят";
    private static final String EMAIL_IS_EXISTING = "Email уже используется";
    private static final String NOT_FOUND_MESSAGE = "Пользователь не найден";

    @Override
    public UserResponse create(CreateUserRequest request) {
        boolean isEmail = userRepository.isEmailExist(request.email());
        boolean isLogin = userRepository.isLoginExist(request.login());
        log.debug("Create user: checking email uniqueness. email={}", request.email());
        if (isEmail) {
            log.warn("Create user rejected: email already exists. email={}", request.email());
            throw new ConditionNotMetException(EMAIL_IS_EXISTING);
        }
        log.debug("Create user: checking login uniqueness. login={}", request.login());
        if (isLogin) {
            log.warn("Create user rejected: login already exists. login={}", request.login());
            throw new ConditionNotMetException(LOGIN_IS_EXISTING);
        }
        log.info("Create user started. login={}, email={}", request.login(), request.email());
        User createdUser = userRepository.create(UserMapper.toUser(request));
        log.info("Create user completed. userId={}, login={}", createdUser.getId(), createdUser.getLogin());

        return UserMapper.toUserResponse(createdUser);
    }

    @Override
    public UserResponse get(Long id) {
        log.debug("Get user by id requested. userId={}", id);
        return userRepository.findOne(id)
                .map(UserMapper::toUserResponse)
                .orElseThrow(() -> {
                    log.warn("Get user failed: user not found. userId={}", id);
                    return new NotFoundException(NOT_FOUND_MESSAGE);
                });
    }

    @Override
    public List<UserResponse> getAll() {
        List<UserResponse> users = userRepository.findAll().stream()
                .map(UserMapper::toUserResponse)
                .toList();
        log.debug("Get all users completed. count={}", users.size());
        return users;
    }

    @Override
    public UserResponse update(UpdateUserRequest request, Long id) {
        log.info("Update user started. userId={}", id);
        User user = userRepository.findOne(id).orElseThrow(() -> {
            log.warn("Update user failed: user not found. userId={}", id);
            return new NotFoundException(NOT_FOUND_MESSAGE);
        });

        String login = user.getLogin();
        String email = user.getEmail();

        log.debug("Update user: checking email uniqueness. userId={}, email={}", id, request.email());
        if (request.hasEmail()) {
            if (!request.email().equals(email)) {
                boolean isEmailValid = userRepository.isEmailExist(request.email());
                if (isEmailValid) {
                    log.warn("Update user rejected: email already exists. userId={}, email={}", id, request.email());
                    throw new ConditionNotMetException(EMAIL_IS_EXISTING);
                }
            }
        }
        log.debug("Update user: checking login uniqueness. userId={}, login={}", id, request.login());
        if (request.hasLogin()) {
            if (!request.login().equals(login)) {
                boolean isLoginValid = userRepository.isLoginExist(request.login());
                if (isLoginValid) {
                    log.warn("Update user rejected: login already exists. userId={}, login={}", id, request.login());
                    throw new ConditionNotMetException(LOGIN_IS_EXISTING);
                }
            }
        }

        log.debug("Update user: mapping request to model. userId={}", id);
        User updatedUser = UserMapper.toUserUpdate(request, user);
        User savedUser = userRepository.update(updatedUser);
        log.info("Update user completed. userId={}, login={}", savedUser.getId(), savedUser.getLogin());
        return UserMapper.toUserResponse(savedUser);
    }

    @Override
    public void delete(Long id) {
        log.info("Delete user started. userId={}", id);
        boolean isDelete = userRepository.delete(id);
        if (!isDelete) {
            log.warn("Delete user failed: user not found. userId={}", id);
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }
        log.info("Delete user completed. userId={}", id);
    }

}
