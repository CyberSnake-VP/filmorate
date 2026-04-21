package example.controller;

import example.dto.request.CreateUserRequest;
import example.dto.request.UpdateUserRequest;
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
        log.info("HTTP GET /users/{} started", id);
        UserResponse response = userService.get(id);
        log.info("HTTP GET /users/{} completed. login={}", id, response.login());
        return response;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        log.info("HTTP DELETE /users/{} started.", id);
        userService.delete(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse update(@PathVariable Long id,
                               @Valid @RequestBody UpdateUserRequest request) {
        log.info("HTTP PUT /users/{} started. ", id);
        UserResponse response = userService.update(request, id);
        log.info("HTTP PUT /users/{} completed. ", id);
        return response;
    }

}
