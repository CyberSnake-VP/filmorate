package example.service;

import example.dal.FriendRepository;
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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    private static final String LOGIN_IS_EXISTING = "Логин уже занят";
    private static final String EMAIL_IS_EXISTING = "Email уже используется";
    private static final String NOT_FOUND_MESSAGE = "Пользователь не найден";
    private static final String ADD_THEMSELVES_MESSAGE = "Нельзя добавить пользователя в друзья самому себе";
    private static final String FRIEND_IS_ALREADY_MESSAGE = "Пользователи уже являются друзьями";
    private static final String REMOVE_THEMSELVES_MESSAGE = "Нельзя удалить из друзей самого себя";


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
        User createUser = UserMapper.toUser(request);
        if (request.name() == null || request.name().isBlank()) {
            createUser.setName(request.login());
        }
        User createdUser = userRepository.create(createUser);
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

    @Override
    public void addFriend(Long userId, Long friendId) {
        log.info("Add friend started: userId={}, friendId={}", userId, friendId);
        log.debug("Add friend: checking self-friend request. userId={}, friendId={}", userId, friendId);
        if (userId.equals(friendId)) {
            log.warn("Add friend failed: user cannot add themselves. userId={}, friendId={}", userId, friendId);
            throw new ConditionNotMetException(ADD_THEMSELVES_MESSAGE);
        }

        boolean isExistUser = userRepository.isExistById(userId);
        boolean isExistFriend = userRepository.isExistById(friendId);

        log.debug("Add friend: checking users existence. userId={}, friendId={}", userId, friendId);
        if (!isExistUser || !isExistFriend) {
            log.warn("Add friend failed: user or friend not found. userId={}, friendId={}", userId, friendId);
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }

        boolean isAlreadyFriends = friendRepository.existFriend(userId, friendId);
        log.debug("Add friend: checking friendship already exists. userId={}, friendId={}", userId, friendId);
        if (isAlreadyFriends) {
            log.warn("Add friend failed: already friends. userId={}, friendId={}", userId, friendId);
            throw new ConditionNotMetException(FRIEND_IS_ALREADY_MESSAGE);
        }

        friendRepository.addFriend(userId, friendId);
        log.info("Add friend completed: userId={}, friendId={}", userId, friendId);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        log.info("Remove friend started: userId={}, friendId={}", userId, friendId);

        if (userId.equals(friendId)) {
            log.warn("Remove friend failed: user cannot remove themselves. userId={}, friendId={}", userId, friendId);
            throw new ConditionNotMetException(REMOVE_THEMSELVES_MESSAGE);
        }

        boolean isExistUser = userRepository.isExistById(userId);
        boolean isExistFriend = userRepository.isExistById(friendId);

        log.debug("Remove friend: checking users existence. userId={}, friendId={}", userId, friendId);
        if (!isExistUser || !isExistFriend) {
            log.warn("Remove friend failed: user or friend not found. userId={}, friendId={}", userId, friendId);
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }

        friendRepository.removeFriend(userId, friendId);
        log.info("Remove friend completed: userId={}, friendId={}", userId, friendId);
    }

    @Override
    public List<UserResponse> getFriends(Long userId) {
        log.info("Get friends started: userId={}", userId);
        boolean isUserExisting = userRepository.isExistById(userId);
        log.debug("Get friends: checking users existence. userId={}", userId);
        if (!isUserExisting) {
            log.warn("Get friends failed: userId={}", userId);
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }
        // получаем id друзей френдов
        log.debug("Get friends: getFriends");
        List<Long> friendsIds = friendRepository.getFriends(userId);
        // получаем запросом список юзеров (friendIds)
        log.debug("Get friends: findAllByIds");
        List<User> friends = userRepository.findAllByIds(friendsIds);

        // получаем маппу с id юзера и список его френдов
        log.debug("Get friends: getFriendsForUsers");
        Map<Long, List<Long>> friendsMap = friendRepository.getFriendsForUsers(friendsIds);

        // пробегаемся по списки друзей (id) и достаем из маппы список друзей
        friends.forEach(friend -> {
            List<Long> friendFriends = friendsMap.getOrDefault(friend.getId(), Collections.emptyList());
            friend.getFriends().addAll(friendFriends);
        });

        List<UserResponse> friendResponses = friends.stream()
                .map(UserMapper::toUserResponse)
                .toList();

        log.info("Get friends completed: userId={}, friendsCount={}", userId, friendResponses.size());
        return friendResponses;
    }

    @Override
    public List<UserResponse> getCommonFriends(Long userId, Long friendId) {
        return List.of();
    }


}
