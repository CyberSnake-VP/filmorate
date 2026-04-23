package example.controller;

import example.dto.request.user.CreateUserRequest;
import example.dto.request.user.UpdateUserRequest;
import example.dto.response.UserResponse;
import example.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody CreateUserRequest request) {
        log.info("HTTP POST /users started. login={}, email={}", request.login(), request.email());
        UserResponse response = userService.create(request);
        log.info("HTTP POST /users completed. userId={}", response.id());
        return response;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getAll() {
        log.info("HTTP GET /users started");
        List<UserResponse> responses = userService.getAll();
        log.info("HTTP GET users/ completed. user count={}", responses.size());
        return responses;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse get(@PathVariable Long id) {
        log.info("HTTP GET /users/{id} started. id={}", id);
        UserResponse response = userService.get(id);
        log.info("HTTP GET /users/{id} completed.  id={}, login={}", id, response.login());
        return response;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        log.info("HTTP DELETE /users/{id} started. id={}", id);
        userService.delete(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse update(@PathVariable Long id,
                               @Valid @RequestBody UpdateUserRequest request) {
        log.info("HTTP PUT /users/{id} started. id={}", id);
        UserResponse response = userService.update(request, id);
        log.info("HTTP PUT /users/{id} completed. id={}", id);
        return response;
    }

    @PutMapping("/{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addFriend(@PathVariable Long userId,
                          @PathVariable Long friendId) {
        log.info("HTTP PUT /users/{userId}/friends/{friendId} started. userId={}, friendId={}" , userId, friendId);
        userService.addFriend(userId, friendId);
        log.info("HTTP PUT /users/{userId}/friends/{friendId} completed. userId={}, friendId={}", userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFriend(@PathVariable Long userId,
                             @PathVariable Long friendId) {
        log.info("HTTP DELETE /users/{userId}/friends/{friendId} started. userId={}, friendId={}", userId, friendId);
        userService.removeFriend(userId, friendId);
        log.info("HTTP DELETE /users/{userId}/friends/{friendId} completed. userId={}, friendId={}", userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getFriends(@PathVariable Long userId) {
        log.info("HTTP GET /users/{userId}/friends} started. userId={}", userId);
        return userService.getFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getCommonFriends(@PathVariable Long userId, @PathVariable Long otherId) {
        log.info("HTTP GET /users/{userId}/friends/common/{otherId} started. userId={}, otherId={}", userId, otherId);
        List<UserResponse> responses = userService.getCommonFriends(userId, otherId);
        log.info("HTTP GET /users/{userId}/friends/common/{otherId} completed. userId={}, otherId={}", userId, otherId);
        return responses;
    }

}
